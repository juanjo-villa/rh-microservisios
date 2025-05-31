
import React, { useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { Clock, Plus, Users, Edit, Trash, CalendarClock } from "lucide-react";
import { toast } from "@/components/ui/sonner";
import { DataTable } from "@/components/DataTable";
import { scheduleApi } from "@/utils/api";

const ScheduleList = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const employeeIdFilter = searchParams.get('employeeId');
  
  // Query to fetch schedules
  const { data: schedules = [], refetch } = useQuery({
    queryKey: ["schedules"],
    queryFn: async () => {
      const response = await scheduleApi.getAll();
      return response.data || [
        { id: 1, name: "Horario matutino", startTime: "08:00", totalHours: 8, deductHours: 1, employeesAssigned: 15 },
        { id: 2, name: "Horario vespertino", startTime: "14:00", totalHours: 8, deductHours: 1, employeesAssigned: 8 },
        { id: 3, name: "Horario fin de semana", startTime: "09:00", totalHours: 9, deductHours: 1, employeesAssigned: 5 },
        { id: 4, name: "Media jornada mañana", startTime: "08:00", totalHours: 4, deductHours: 0, employeesAssigned: 3 },
        { id: 5, name: "Turno nocturno", startTime: "22:00", totalHours: 8, deductHours: 1, employeesAssigned: 7 },
      ];
    },
  });

  // Handle delete schedule
  const handleDeleteSchedule = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este horario?")) {
      try {
        await scheduleApi.delete(id);
        toast.success("Horario eliminado con éxito");
        refetch();
      } catch (error) {
        toast.error("Error al eliminar el horario");
        console.error("Error deleting schedule:", error);
      }
    }
  };

  // Calculate end time
  const calculateEndTime = (startTime: string, hours: number) => {
    if (!startTime) return "";
    
    const [startHour, startMinute] = startTime.split(":").map(Number);
    let endHour = startHour + hours;
    const endMinute = startMinute;
    
    if (endHour >= 24) {
      endHour = endHour % 24;
    }
    
    return `${String(endHour).padStart(2, "0")}:${String(endMinute).padStart(2, "0")}`;
  };

  // Define table columns
  const columns = [
    {
      key: "name",
      header: "Nombre",
      cell: (schedule: any) => (
        <div className="font-medium">{schedule.name}</div>
      ),
      sortable: true,
    },
    {
      key: "hours",
      header: "Horario",
      cell: (schedule: any) => {
        const endTime = calculateEndTime(schedule.startTime, schedule.totalHours);
        return `${schedule.startTime} - ${endTime}`;
      },
      sortable: false,
    },
    {
      key: "totalHours",
      header: "Horas",
      cell: (schedule: any) => (
        <div className="flex items-center">
          <Clock size={16} className="mr-2 text-blue-500" />
          {schedule.totalHours}
          {schedule.deductHours > 0 && (
            <span className="text-xs text-muted-foreground ml-1">
              (-{schedule.deductHours})
            </span>
          )}
        </div>
      ),
      sortable: true,
    },
    {
      key: "employeesAssigned",
      header: "Empleados asignados",
      cell: (schedule: any) => (
        <div className="flex items-center">
          <Users size={16} className="mr-2 text-blue-500" />
          {schedule.employeesAssigned || 0}
        </div>
      ),
      sortable: true,
    },
  ];
  
  // Define table actions
  const actions = (schedule: any) => (
    <div className="flex space-x-2">
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(`/admin/schedules/${schedule.id}/edit`)}
      >
        <Edit size={16} />
        <span className="sr-only">Editar</span>
      </Button>
      <Button
        variant="ghost"
        size="sm"
        onClick={() => handleDeleteSchedule(schedule.id)}
      >
        <Trash size={16} />
        <span className="sr-only">Eliminar</span>
      </Button>
      <Button
        variant="outline"
        size="sm"
      >
        <Users size={14} className="mr-1" />
        <span className="hidden sm:inline">Asignar</span>
      </Button>
    </div>
  );

  // Upcoming assignments table data (mock)
  const upcomingAssignments = [
    {
      employee: "Juan Pérez",
      schedule: "Horario matutino",
      startDate: "15/05/2025",
      status: "Activo"
    },
    {
      employee: "María García",
      schedule: "Media jornada mañana",
      startDate: "18/05/2025",
      status: "Pendiente"
    }
  ];

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Gestión de Horarios</h1>
        <Link to="/admin/schedules/new">
          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Nuevo Horario
          </Button>
        </Link>
      </div>

      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Horarios Actuales</CardTitle>
        </CardHeader>
        <CardContent>
          <DataTable
            data={schedules}
            columns={columns}
            actions={actions}
            searchable
            searchKeys={["name"]}
          />
        </CardContent>
      </Card>

      <div className="mt-6">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle>Próximas asignaciones</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center space-x-2 text-sm text-muted-foreground mb-4">
              <Clock size={16} />
              <span>Los cambios en los horarios se aplicarán automáticamente en la próxima jornada laboral.</span>
            </div>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Empleado</TableHead>
                  <TableHead>Horario asignado</TableHead>
                  <TableHead>Fecha de inicio</TableHead>
                  <TableHead>Estado</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {upcomingAssignments.map((assignment, index) => (
                  <TableRow key={index}>
                    <TableCell>{assignment.employee}</TableCell>
                    <TableCell>{assignment.schedule}</TableCell>
                    <TableCell>{assignment.startDate}</TableCell>
                    <TableCell>
                      <span className={`px-2 py-1 rounded-md text-xs ${
                        assignment.status === 'Activo' 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-amber-100 text-amber-800'
                      }`}>
                        {assignment.status}
                      </span>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default ScheduleList;
