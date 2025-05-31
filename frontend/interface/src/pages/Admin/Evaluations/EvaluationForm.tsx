
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
import { Slider } from "@/components/ui/slider";
import { toast } from "@/components/ui/sonner";
import { evaluationApi, employeeApi } from "@/utils/api";

// Schema for form validation
const evaluationSchema = z.object({
  employeeId: z.string().min(1, "Seleccione un empleado"),
  date: z.string().refine(val => val.length > 0, {
    message: "La fecha es requerida",
  }),
  score: z.number().min(1).max(5),
  comments: z.string().min(10, "Los comentarios deben tener al menos 10 caracteres"),
});

type EvaluationFormValues = z.infer<typeof evaluationSchema>;

const EvaluationForm = () => {
  const { id } = useParams();
  const isEditMode = !!id;
  const navigate = useNavigate();

  // Form initialization
  const form = useForm<EvaluationFormValues>({
    resolver: zodResolver(evaluationSchema),
    defaultValues: {
      employeeId: "",
      date: new Date().toISOString().split('T')[0], // Today's date as default
      score: 3,
      comments: "",
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

  // Fetch evaluation if in edit mode
  useQuery({
    queryKey: ["evaluation", id],
    queryFn: async () => {
      if (!isEditMode) return null;
      const response = await evaluationApi.getById(Number(id));
      const evaluation = response.data;

      // Populate the form
      form.reset({
        employeeId: String(evaluation.employeeId),
        date: new Date(evaluation.date).toISOString().split('T')[0],
        score: evaluation.score,
        comments: evaluation.comments,
      });

      return evaluation;
    },
    enabled: isEditMode,
  });

  const onSubmit = async (data: EvaluationFormValues) => {
    try {
      const evaluationData = {
        ...data,
        employeeId: Number(data.employeeId),
      };

      if (isEditMode) {
        await evaluationApi.update(Number(id), evaluationData);
        toast.success("Evaluación actualizada con éxito");
      } else {
        await evaluationApi.create(evaluationData);
        toast.success("Evaluación creada con éxito");
      }
      navigate("/admin/evaluations");
    } catch (error) {
      toast.error("Error al guardar la evaluación");
      console.error("Error saving evaluation:", error);
    }
  };

  // Get current score for display
  const currentScore = form.watch("score");

  // Helper function to get score label
  const getScoreLabel = (score: number) => {
    switch (score) {
      case 1: return "Insuficiente";
      case 2: return "Regular";
      case 3: return "Bueno";
      case 4: return "Muy Bueno";
      case 5: return "Excelente";
      default: return "";
    }
  };

  return (
    <div className="page-container">
      <Card>
        <CardHeader>
          <CardTitle>{isEditMode ? "Editar Evaluación" : "Nueva Evaluación"}</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
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
                        disabled={isEditMode}
                      >
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Seleccione un empleado" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {employees.map((employee) => (
                            <SelectItem key={employee.id} value={String(employee.id)}>
                              {employee.name} {employee.lastname}
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
                  name="date"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Fecha de evaluación</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <FormField
                control={form.control}
                name="score"
                render={({ field: { value, onChange } }) => (
                  <FormItem>
                    <FormLabel>Puntuación: {currentScore} - {getScoreLabel(currentScore)}</FormLabel>
                    <FormControl>
                      <div className="pt-2 px-1">
                        <Slider
                          min={1}
                          max={5}
                          step={1}
                          defaultValue={[value]}
                          onValueChange={(vals) => onChange(vals[0])}
                        />
                        <div className="flex justify-between mt-2 text-xs text-muted-foreground">
                          <span>Insuficiente</span>
                          <span>Regular</span>
                          <span>Bueno</span>
                          <span>Muy Bueno</span>
                          <span>Excelente</span>
                        </div>
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="comments"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Comentarios</FormLabel>
                    <FormControl>
                      <Textarea 
                        placeholder="Comentarios detallados sobre el desempeño del empleado" 
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
                  onClick={() => navigate("/admin/evaluations")}
                >
                  Cancelar
                </Button>
                <Button type="submit">
                  {isEditMode ? "Actualizar Evaluación" : "Crear Evaluación"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};

export default EvaluationForm;
