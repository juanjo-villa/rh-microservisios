
import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage, FormDescription } from "@/components/ui/form";
import { toast } from "@/components/ui/sonner";
import { scheduleApi, employeeApi } from "@/utils/api";
import { Checkbox } from "@/components/ui/checkbox";
import { MultiSelect } from "@/components/ui/multi-select";

// Schema for form validation
const scheduleSchema = z.object({
  date: z.string().min(1, "La fecha es requerida"),
  startTime: z.string().min(5, "Seleccione una hora de inicio"),
  exitTime: z.string().min(5, "Seleccione una hora de fin"),
  totalHours: z.string().refine((val) => !isNaN(Number(val)) && Number(val) > 0, {
    message: "Las horas totales deben ser un número positivo",
  }),
  deductedHours: z.string().refine((val) => !isNaN(Number(val)) && Number(val) >= 0, {
    message: "Las horas a deducir deben ser un número positivo o cero",
  }),
  employeeIds: z.array(z.number()).optional(),
});

type ScheduleFormValues = z.infer<typeof scheduleSchema>;

const ScheduleForm = () => {
  const { id } = useParams();
  const isEditMode = !!id;
  const navigate = useNavigate();
  const [selectedEmployees, setSelectedEmployees] = useState<{ value: number; label: string }[]>([]);

  // Form initialization
  const form = useForm<ScheduleFormValues>({
    resolver: zodResolver(scheduleSchema),
    defaultValues: {
      date: new Date().toISOString().split('T')[0],
      startTime: "09:00",
      exitTime: "17:00",
      totalHours: "8",
      deductedHours: "0",
      employeeIds: [],
    },
  });

  // Fetch employees for the dropdown
  const { data: employees = [] } = useQuery({
    queryKey: ["employees"],
    queryFn: async () => {
      const response = await employeeApi.getAll();
      return response.data || [];
    },
  });

  // Convert employees to options for the multi-select
  const employeeOptions = employees.map(employee => ({
    value: employee.id!,
    label: `${employee.name} ${employee.lastName}`
  }));

  // Fetch schedule if in edit mode
  useQuery({
    queryKey: ["schedule", id],
    queryFn: async () => {
      if (!isEditMode) return null;
      const response = await scheduleApi.getById(Number(id));
      const schedule = response.data;

      // Populate the form
      form.reset({
        date: schedule.date,
        startTime: schedule.startTime,
        exitTime: schedule.exitTime,
        totalHours: String(schedule.totalHours),
        deductedHours: String(schedule.deductedHours),
        employeeIds: schedule.employeeIds || [],
      });

      // Set selected employees
      if (schedule.employeeIds && schedule.employeeIds.length > 0) {
        const selected = employeeOptions.filter(option => 
          schedule.employeeIds?.includes(option.value)
        );
        setSelectedEmployees(selected);
      }

      return schedule;
    },
    enabled: isEditMode && employeeOptions.length > 0,
  });

  const onSubmit = async (data: ScheduleFormValues) => {
    try {
      const scheduleData = {
        date: data.date,
        startTime: data.startTime,
        exitTime: data.exitTime,
        totalHours: Number(data.totalHours),
        deductedHours: Number(data.deductedHours),
        employeeIds: selectedEmployees.map(emp => emp.value),
      };

      if (isEditMode) {
        await scheduleApi.update(Number(id), scheduleData);
        toast.success("Horario actualizado con éxito");
      } else {
        await scheduleApi.create(scheduleData);
        toast.success("Horario creado con éxito");
      }
      navigate("/admin/schedules");
    } catch (error) {
      toast.error("Error al guardar el horario");
      console.error("Error saving schedule:", error);
    }
  };

  // Calculate end time based on start time and total hours
  const calculateEndTime = (startTime: string, hours: string) => {
    if (!startTime || !hours || isNaN(Number(hours))) return "";

    const [startHour, startMinute] = startTime.split(":").map(Number);
    let endHour = startHour + Number(hours);
    const endMinute = startMinute;

    if (endHour >= 24) {
      endHour = endHour % 24;
    }

    return `${String(endHour).padStart(2, "0")}:${String(endMinute).padStart(2, "0")}`;
  };

  const startTime = form.watch("startTime");
  const totalHours = form.watch("totalHours");
  const endTime = calculateEndTime(startTime, totalHours);

  return (
    <div className="page-container">
      <Card>
        <CardHeader>
          <CardTitle>{isEditMode ? "Editar Horario" : "Nuevo Horario"}</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <FormField
                control={form.control}
                name="date"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Fecha del horario</FormLabel>
                    <FormControl>
                      <Input type="date" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FormField
                  control={form.control}
                  name="startTime"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Hora de inicio</FormLabel>
                      <FormControl>
                        <Input type="time" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="exitTime"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Hora de fin</FormLabel>
                      <FormControl>
                        <Input type="time" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FormField
                  control={form.control}
                  name="totalHours"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Horas totales</FormLabel>
                      <FormControl>
                        <Input type="number" min="1" step="0.5" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="deductedHours"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Horas a deducir</FormLabel>
                      <FormControl>
                        <Input type="number" min="0" step="0.5" {...field} />
                      </FormControl>
                      <FormDescription>
                        Horas que se deducirán automáticamente (ej: tiempo de almuerzo)
                      </FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <FormField
                control={form.control}
                name="employeeIds"
                render={() => (
                  <FormItem>
                    <FormLabel>Asignar Empleados</FormLabel>
                    <FormControl>
                      <MultiSelect
                        options={employeeOptions}
                        selected={selectedEmployees}
                        onChange={setSelectedEmployees}
                        placeholder="Seleccionar empleados..."
                      />
                    </FormControl>
                    <FormDescription>
                      Seleccione los empleados que serán asignados a este horario
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="flex justify-end space-x-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => navigate("/admin/schedules")}
                >
                  Cancelar
                </Button>
                <Button type="submit">
                  {isEditMode ? "Actualizar Horario" : "Crear Horario"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};

export default ScheduleForm;
