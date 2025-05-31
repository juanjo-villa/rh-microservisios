
import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { toast } from "@/components/ui/sonner";
import { employeeApi, positionApi } from "@/utils/api";

// Schema for form validation
const createEmployeeSchema = z.object({
  dni: z.string()
    .min(1, "El DNI debe tener al menos 1 caracter")
    .max(10, "El DNI no debe exceder 10 caracteres")
    .regex(/^[0-9]+$/, "El DNI debe contener solo números"),
  name: z.string().min(3, "El nombre debe tener al menos 3 caracteres").max(100, "El nombre no debe exceder 100 caracteres"),
  lastName: z.string().min(3, "El apellido debe tener al menos 3 caracteres").max(100, "El apellido no debe exceder 100 caracteres"),
  address: z.string().min(3, "La dirección debe tener al menos 3 caracteres").max(100, "La dirección no debe exceder 100 caracteres"),
  email: z.string().email("Ingrese un email válido"),
  phone: z.string()
    .max(11, "El teléfono no debe exceder 11 dígitos")
    .regex(/^[0-9]+$/, "El teléfono debe contener solo números"),
  password: z.string()
    .min(8, "La contraseña debe tener al menos 8 caracteres")
    .max(100, "La contraseña no debe exceder 100 caracteres")
    .regex(/^(?=.*\d)(?=.*[@#$%^&+=!]).+$/, "La contraseña debe contener al menos un número y un caracter especial"),
  positionId: z.string().min(1, "Seleccione un cargo"),
});

// Schema for editing an employee (password is optional)
const updateEmployeeSchema = z.object({
  dni: z.string()
    .min(1, "El DNI debe tener al menos 1 caracter")
    .max(10, "El DNI no debe exceder 10 caracteres")
    .regex(/^[0-9]+$/, "El DNI debe contener solo números"),
  name: z.string().min(3, "El nombre debe tener al menos 3 caracteres").max(100, "El nombre no debe exceder 100 caracteres"),
  lastName: z.string().min(3, "El apellido debe tener al menos 3 caracteres").max(100, "El apellido no debe exceder 100 caracteres"),
  address: z.string().min(3, "La dirección debe tener al menos 3 caracteres").max(100, "La dirección no debe exceder 100 caracteres"),
  email: z.string().email("Ingrese un email válido"),
  phone: z.string()
    .max(11, "El teléfono no debe exceder 11 dígitos")
    .regex(/^[0-9]+$/, "El teléfono debe contener solo números"),
  password: z.string()
    .max(100, "La contraseña no debe exceder 100 caracteres")
    .regex(/^(?=.*\d)(?=.*[@#$%^&+=!]).+$/, "La contraseña debe contener al menos un número y un caracter especial")
    .or(z.literal("")), // Allow empty string for password in edit mode
  positionId: z.string().min(1, "Seleccione un cargo"),
});

type CreateEmployeeFormValues = z.infer<typeof createEmployeeSchema>;
type UpdateEmployeeFormValues = z.infer<typeof updateEmployeeSchema>;
type EmployeeFormValues = CreateEmployeeFormValues | UpdateEmployeeFormValues;

const EmployeeForm = () => {
  const { id } = useParams();
  const isEditMode = !!id;
  const navigate = useNavigate();

  // Form initialization with the appropriate schema based on mode
  const form = useForm<EmployeeFormValues>({
    resolver: zodResolver(isEditMode ? updateEmployeeSchema : createEmployeeSchema),
    defaultValues: {
      dni: "",
      name: "",
      lastName: "",
      address: "",
      email: "",
      phone: "",
      password: "",
      positionId: "",
    },
  });

  // Fetch positions for dropdown
  const { data: positions = [] } = useQuery({
    queryKey: ["positions"],
    queryFn: async () => {
      const response = await positionApi.getAll();
      return response.data || [];
    },
  });

  // Fetch employee if in edit mode
  useQuery({
    queryKey: ["employee", id],
    queryFn: async () => {
      if (!isEditMode) return null;
      const response = await employeeApi.getById(Number(id));
      const employee = response.data;

      // Populate the form
      form.reset({
        dni: employee.dni,
        name: employee.name,
        lastName: employee.lastName,
        address: employee.address,
        email: employee.email,
        phone: employee.phone,
        password: "", // Don't populate password for security reasons
        positionId: String(employee.positionId),
      });

      return employee;
    },
    enabled: isEditMode,
  });

  const onSubmit = async (data: EmployeeFormValues) => {
    try {
      // Convert positionId to number
      let employeeData = {
        ...data,
        positionId: Number(data.positionId),
      };

      // In edit mode, if password is empty, remove it from the data
      if (isEditMode && !data.password) {
        const { password, ...dataWithoutPassword } = employeeData;
        employeeData = dataWithoutPassword;
      }

      if (isEditMode) {
        await employeeApi.update(Number(id), employeeData);
        toast.success("Empleado actualizado con éxito");
      } else {
        await employeeApi.create(employeeData);
        toast.success("Empleado creado con éxito");
      }
      navigate("/admin/employees");
    } catch (error) {
      toast.error("Error al guardar el empleado", {
        description: error instanceof Error ? error.message : "Ocurrió un error inesperado"
      });
      console.error("Error saving employee:", error);
    }
  };

  return (
    <div className="page-container">
      <Card>
        <CardHeader>
          <CardTitle>{isEditMode ? "Editar Empleado" : "Nuevo Empleado"}</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FormField
                  control={form.control}
                  name="dni"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>DNI</FormLabel>
                      <FormControl>
                        <Input placeholder="Número de identificación" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Nombre</FormLabel>
                      <FormControl>
                        <Input placeholder="Nombre del empleado" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="lastName"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Apellidos</FormLabel>
                      <FormControl>
                        <Input placeholder="Apellidos del empleado" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Email</FormLabel>
                      <FormControl>
                        <Input type="email" placeholder="correo@ejemplo.com" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="phone"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Teléfono</FormLabel>
                      <FormControl>
                        <Input placeholder="Número telefónico" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="address"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Dirección</FormLabel>
                      <FormControl>
                        <Input placeholder="Dirección completa" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />


                <FormField
                  control={form.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>{isEditMode ? "Nueva contraseña" : "Contraseña"}</FormLabel>
                      <FormControl>
                        <Input type="password" placeholder={isEditMode ? "Dejar en blanco para mantener" : "Contraseña"} {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="positionId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Cargo</FormLabel>
                      <Select
                        onValueChange={field.onChange}
                        defaultValue={field.value}
                        value={field.value}
                      >
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Seleccione un cargo" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {positions.map((position) => (
                            <SelectItem key={position.id} value={String(position.id)}>
                              {position.name}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <div className="flex justify-end space-x-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => navigate("/admin/employees")}
                >
                  Cancelar
                </Button>
                <Button type="submit">
                  {isEditMode ? "Actualizar Empleado" : "Crear Empleado"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};

export default EmployeeForm;
