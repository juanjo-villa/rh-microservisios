import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Checkbox } from "@/components/ui/checkbox";
import { toast } from "@/components/ui/sonner";
import { statusApi, employeeApi } from "@/utils/api";
import { format } from "date-fns";

// Schema for form validation
const statusSchema = z.object({
  type: z.string()
    .min(3, "El tipo debe tener al menos 3 caracteres")
    .max(50, "El tipo no debe exceder 50 caracteres"),
  startDate: z.string().min(1, "La fecha de inicio es requerida"),
  endDate: z.string().min(1, "La fecha de fin es requerida"),
  paid: z.boolean().default(false),
  description: z.string()
    .min(3, "La descripción debe tener al menos 3 caracteres")
    .max(250, "La descripción no debe exceder 250 caracteres"),
  employeeId: z.string().min(1, "Seleccione un empleado"),
  statusPermissionId: z.string().min(1, "Seleccione un permiso"),
});

type StatusFormValues = z.infer<typeof statusSchema>;

// Mock status permissions for dropdown (in a real app, this would come from an API)
const statusPermissions = [
  { id: 1, name: "Vacaciones" },
  { id: 2, name: "Permiso médico" },
  { id: 3, name: "Licencia" },
  { id: 4, name: "Ausencia justificada" },
];

const StatusForm = () => {
  const { id } = useParams();
  const isEditMode = !!id;
  const navigate = useNavigate();

  // Form initialization
  const form = useForm<StatusFormValues>({
    resolver: zodResolver(statusSchema),
    defaultValues: {
      type: "",
      startDate: format(new Date(), "yyyy-MM-dd"),
      endDate: format(new Date(), "yyyy-MM-dd"),
      paid: false,
      description: "",
      employeeId: "",
      statusPermissionId: "",
    },
  });

  // Fetch employees for dropdown
  const { data: employees = [] } = useQuery({
    queryKey: ["employees"],
    queryFn: async () => {
      const response = await employeeApi.getAll();
      return response.data || [];
    },
  });

  // Fetch status if in edit mode
  useQuery({
    queryKey: ["status", id],
    queryFn: async () => {
      if (!isEditMode) return null;
      const response = await statusApi.getById(Number(id));
      const status = response.data;

      // Populate the form
      form.reset({
        type: status.type,
        startDate: format(new Date(status.startDate), "yyyy-MM-dd"),
        endDate: format(new Date(status.endDate), "yyyy-MM-dd"),
        paid: Boolean(status.paid),
        description: status.description,
        employeeId: String(status.employeeId),
        statusPermissionId: String(status.statusPermissionId),
      });

      return status;
    },
    enabled: isEditMode,
  });

  const onSubmit = async (data: StatusFormValues) => {
    try {
      const statusData = {
        ...data,
        paid: data.paid ? 1 : 0,
        employeeId: Number(data.employeeId),
        statusPermissionId: Number(data.statusPermissionId),
        startDate: new Date(data.startDate),
        endDate: new Date(data.endDate),
      };

      if (isEditMode) {
        await statusApi.update(Number(id), statusData);
        toast.success("Estado actualizado con éxito");
      } else {
        await statusApi.create(statusData);
        toast.success("Estado creado con éxito");
      }
      navigate("/admin/status");
    } catch (error) {
      toast.error("Error al guardar el estado", {
        description: error instanceof Error ? error.message : "Ocurrió un error inesperado"
      });
      console.error("Error saving status:", error);
    }
  };

  return (
    <div className="page-container">
      <Card>
        <CardHeader>
          <CardTitle>{isEditMode ? "Editar Estado" : "Nuevo Estado"}</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FormField
                  control={form.control}
                  name="type"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Tipo</FormLabel>
                      <FormControl>
                        <Input placeholder="Tipo de estado" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="employeeId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Empleado</FormLabel>
                      <Select
                        onValueChange={field.onChange}
                        defaultValue={field.value}
                        value={field.value}
                      >
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Seleccione un empleado" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {employees.map((employee) => (
                            <SelectItem key={employee.id} value={String(employee.id)}>
                              {employee.name} {employee.lastName}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="statusPermissionId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Permiso</FormLabel>
                      <Select
                        onValueChange={field.onChange}
                        defaultValue={field.value}
                        value={field.value}
                      >
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Seleccione un permiso" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {statusPermissions.map((permission) => (
                            <SelectItem key={permission.id} value={String(permission.id)}>
                              {permission.name}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="startDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Fecha de inicio</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="endDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Fecha de fin</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="paid"
                  render={({ field }) => (
                    <FormItem className="flex flex-row items-start space-x-3 space-y-0 rounded-md border p-4">
                      <FormControl>
                        <Checkbox
                          checked={field.value}
                          onCheckedChange={field.onChange}
                        />
                      </FormControl>
                      <div className="space-y-1 leading-none">
                        <FormLabel>Pagado</FormLabel>
                        <p className="text-sm text-muted-foreground">
                          Marque esta casilla si el estado es pagado
                        </p>
                      </div>
                    </FormItem>
                  )}
                />
              </div>

              <FormField
                control={form.control}
                name="description"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Descripción</FormLabel>
                    <FormControl>
                      <Textarea 
                        placeholder="Descripción detallada del estado" 
                        className="min-h-32"
                        {...field} 
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="flex justify-end space-x-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => navigate("/admin/status")}
                >
                  Cancelar
                </Button>
                <Button type="submit">
                  {isEditMode ? "Actualizar Estado" : "Crear Estado"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};

export default StatusForm;