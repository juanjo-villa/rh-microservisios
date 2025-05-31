
import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from "@/components/ui/card";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { Calendar, Check, Clock, File, FilePlus, FileText, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { toast } from "@/components/ui/sonner";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { format } from "date-fns";

// Define request schema for form validation
const requestSchema = z.object({
  type: z.string({
    required_error: "Please select a request type",
  }),
  startDate: z.string({
    required_error: "Please select a start date",
  }),
  endDate: z.string({
    required_error: "Please select an end date",
  }),
  days: z.coerce.number()
    .min(0.5, "Days must be at least 0.5")
    .max(30, "Days cannot exceed 30"),
  reason: z.string()
    .min(5, "Reason must be at least 5 characters")
    .max(500, "Reason cannot exceed 500 characters"),
});

type RequestFormValues = z.infer<typeof requestSchema>;

// Define Request interface
interface Request {
  id: number; 
  type: string;
  startDate: string;
  endDate: string;
  days: number;
  reason: string;
  status: string;
  submittedDate: string;
  comments: string;
}

const MyRequests = () => {
  // State for requests list
  const [requests, setRequests] = useState<Request[]>([
    { 
      id: 1, 
      type: "Vacaciones", 
      startDate: "12/06/2025", 
      endDate: "26/06/2025", 
      days: 10, 
      reason: "Vacaciones familiares anuales",
      status: "Pendiente", 
      submittedDate: "28/04/2025",
      comments: "" 
    },
    { 
      id: 2, 
      type: "Permiso", 
      startDate: "10/04/2025", 
      endDate: "10/04/2025", 
      days: 1, 
      reason: "Cita médica",
      status: "Aprobado", 
      submittedDate: "05/04/2025",
      comments: "Permiso aprobado. Por favor asegúrate de actualizar tus tareas pendientes antes de ausentarte." 
    },
    { 
      id: 3, 
      type: "Permiso", 
      startDate: "02/03/2025", 
      endDate: "02/03/2025", 
      days: 0.5, 
      reason: "Asuntos personales (medio día)",
      status: "Rechazado", 
      submittedDate: "25/02/2025",
      comments: "Rechazado debido a la carga de trabajo y plazos ajustados para esa semana." 
    },
  ]);

  // Mock data for balance (available days)
  const availableDays = {
    vacation: 15,
    personal: 3,
    sick: 5
  };

  // Display form state
  const [showRequestForm, setShowRequestForm] = useState(false);

  // Initialize form with react-hook-form
  const form = useForm<RequestFormValues>({
    resolver: zodResolver(requestSchema),
    defaultValues: {
      type: "",
      startDate: format(new Date(), "yyyy-MM-dd"),
      endDate: format(new Date(), "yyyy-MM-dd"),
      days: 1,
      reason: "",
    },
  });

  // Handle form submission
  const onSubmit = (values: RequestFormValues) => {
    // Create new request object
    const newRequest: Request = {
      id: Date.now(),
      type: values.type === "vacation" ? "Vacaciones" : values.type === "personal" ? "Permiso" : "Permiso médico",
      startDate: format(new Date(values.startDate), "dd/MM/yyyy"),
      endDate: format(new Date(values.endDate), "dd/MM/yyyy"),
      days: values.days,
      reason: values.reason,
      status: "Pendiente",
      submittedDate: format(new Date(), "dd/MM/yyyy"),
      comments: ""
    };

    // Add to requests list
    setRequests([newRequest, ...requests]);
    
    // Reset form and hide it
    form.reset();
    setShowRequestForm(false);
    
    // Show success message
    toast.success("Request submitted successfully", {
      description: `Your ${newRequest.type.toLowerCase()} request has been submitted for approval.`
    });
  };

  // Handle date changes to automatically calculate days
  const updateDaysFromDates = () => {
    const startDate = form.watch("startDate");
    const endDate = form.watch("endDate");
    
    if (startDate && endDate) {
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end.getTime() - start.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; // +1 to include both start and end days
      
      if (diffDays > 0) {
        form.setValue("days", diffDays);
      }
    }
  };

  // Watch for changes in start and end dates
  React.useEffect(() => {
    const subscription = form.watch((value, { name }) => {
      if (name === "startDate" || name === "endDate") {
        updateDaysFromDates();
      }
    });
    return () => subscription.unsubscribe();
  }, [form.watch]);

  // Function to get status style
  const getStatusStyle = (status: string) => {
    switch (status) {
      case "Aprobado":
        return "bg-green-100 text-green-800";
      case "Rechazado":
        return "bg-red-100 text-red-800";
      default:
        return "bg-amber-100 text-amber-800";
    }
  };

  // Function to get status icon
  const getStatusIcon = (status: string) => {
    switch (status) {
      case "Aprobado":
        return <Check size={16} className="mr-2 text-green-600" />;
      case "Rechazado":
        return <X size={16} className="mr-2 text-red-600" />;
      default:
        return <Clock size={16} className="mr-2 text-amber-600" />;
    }
  };

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Mis Solicitudes</h1>
        <Button onClick={() => setShowRequestForm(!showRequestForm)}>
          <FilePlus className="mr-2 h-4 w-4" />
          Nueva solicitud
        </Button>
      </div>

      {showRequestForm && (
        <Card className="mb-6">
          <CardHeader>
            <CardTitle>Nueva solicitud</CardTitle>
            <CardDescription>Complete los detalles de su solicitud</CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form id="request-form" onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <FormField
                    control={form.control}
                    name="type"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Tipo de solicitud</FormLabel>
                        <Select 
                          onValueChange={field.onChange} 
                          defaultValue={field.value}
                        >
                          <FormControl>
                            <SelectTrigger>
                              <SelectValue placeholder="Seleccione un tipo" />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            <SelectItem value="vacation">
                              Vacaciones ({availableDays.vacation} días disponibles)
                            </SelectItem>
                            <SelectItem value="personal">
                              Permiso personal ({availableDays.personal} días disponibles)
                            </SelectItem>
                            <SelectItem value="sick">
                              Permiso médico ({availableDays.sick} días disponibles)
                            </SelectItem>
                          </SelectContent>
                        </Select>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  
                  <FormField
                    control={form.control}
                    name="days"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Número de días</FormLabel>
                        <FormControl>
                          <Input 
                            type="number" 
                            min="0.5" 
                            step="0.5" 
                            {...field}
                            onChange={(e) => {
                              field.onChange(parseFloat(e.target.value));
                            }}
                          />
                        </FormControl>
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
                    name="reason"
                    render={({ field }) => (
                      <FormItem className="md:col-span-2">
                        <FormLabel>Motivo</FormLabel>
                        <FormControl>
                          <Textarea 
                            placeholder="Describa el motivo de su solicitud" 
                            rows={3}
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
              </form>
            </Form>
          </CardContent>
          <CardFooter className="flex justify-end space-x-2">
            <Button variant="outline" onClick={() => setShowRequestForm(false)}>Cancelar</Button>
            <Button type="submit" form="request-form">Enviar solicitud</Button>
          </CardFooter>
        </Card>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <Card>
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-muted-foreground text-sm mb-1">Días de vacaciones</p>
                <h3 className="text-xl font-bold">{availableDays.vacation} días</h3>
              </div>
              <div className="h-10 w-10 bg-blue-100 rounded-full flex items-center justify-center">
                <Calendar className="h-5 w-5 text-blue-800" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-muted-foreground text-sm mb-1">Permisos personales</p>
                <h3 className="text-xl font-bold">{availableDays.personal} días</h3>
              </div>
              <div className="h-10 w-10 bg-purple-100 rounded-full flex items-center justify-center">
                <File className="h-5 w-5 text-purple-800" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-muted-foreground text-sm mb-1">Permisos médicos</p>
                <h3 className="text-xl font-bold">{availableDays.sick} días</h3>
              </div>
              <div className="h-10 w-10 bg-green-100 rounded-full flex items-center justify-center">
                <FileText className="h-5 w-5 text-green-800" />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Historial de solicitudes</CardTitle>
          <CardDescription>Todas sus solicitudes y su estado actual</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Tipo</TableHead>
                <TableHead>Fechas</TableHead>
                <TableHead>Días</TableHead>
                <TableHead>Enviado</TableHead>
                <TableHead>Estado</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {requests.map(request => (
                <TableRow key={request.id} className="cursor-pointer hover:bg-muted/50">
                  <TableCell>
                    {request.type}
                    <div className="text-xs text-muted-foreground line-clamp-1">
                      {request.reason}
                    </div>
                  </TableCell>
                  <TableCell>
                    <div>{request.startDate}</div>
                    {request.startDate !== request.endDate && (
                      <div className="text-xs text-muted-foreground">
                        hasta {request.endDate}
                      </div>
                    )}
                  </TableCell>
                  <TableCell>{request.days}</TableCell>
                  <TableCell>{request.submittedDate}</TableCell>
                  <TableCell>
                    <div className="flex items-center">
                      {getStatusIcon(request.status)}
                      <span className={`px-2 py-1 rounded-md text-xs ${getStatusStyle(request.status)}`}>
                        {request.status}
                      </span>
                    </div>
                    {request.comments && (
                      <div className="text-xs text-muted-foreground mt-1 line-clamp-1">
                        {request.comments}
                      </div>
                    )}
                  </TableCell>
                </TableRow>
              ))}
              {requests.length === 0 && (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-8 text-muted-foreground">
                    No hay solicitudes registradas
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
};

export default MyRequests;
