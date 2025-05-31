
import React, { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { Calendar, Check, Clock, Filter, Mail, Search, User, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { statusApi, employeeApi } from "@/utils/api";
import { toast } from "@/components/ui/sonner";
import { format } from "date-fns";
import { es } from "date-fns/locale";

interface StatusWithEmployee {
  id: number;
  type: string;
  startDate: Date;
  endDate: Date;
  paid: number;
  description: string;
  employeeId: number;
  statusPermissionId: number;
  employee?: {
    name: string;
    lastName: string;
  };
  status?: string; // Pending, Approved, Rejected
  submittedDate?: string;
}

const RequestList = () => {
  const [requests, setRequests] = useState<StatusWithEmployee[]>([]);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    pending: 0,
    approved: 0,
    rejected: 0
  });

  // Fetch status requests
  useEffect(() => {
    const fetchRequests = async () => {
      try {
        setLoading(true);
        const response = await statusApi.getAll();
        const statuses = response.data;

        // Fetch employee details for each status
        const statusesWithEmployees = await Promise.all(
          statuses.map(async (status: StatusWithEmployee) => {
            try {
              const employeeResponse = await employeeApi.getById(status.employeeId);
              return {
                ...status,
                employee: {
                  name: employeeResponse.data.name,
                  lastName: employeeResponse.data.lastName
                },
                // For demo purposes, assign random status
                status: ["Pendiente", "Aprobado", "Rechazado"][Math.floor(Math.random() * 3)],
                submittedDate: format(new Date(), "dd/MM/yyyy", { locale: es })
              };
            } catch (error) {
              console.error(`Error fetching employee ${status.employeeId}:`, error);
              return {
                ...status,
                employee: { name: "Usuario", lastName: "Desconocido" },
                status: "Pendiente",
                submittedDate: format(new Date(), "dd/MM/yyyy", { locale: es })
              };
            }
          })
        );

        setRequests(statusesWithEmployees);

        // Calculate stats
        const pending = statusesWithEmployees.filter(r => r.status === "Pendiente").length;
        const approved = statusesWithEmployees.filter(r => r.status === "Aprobado").length;
        const rejected = statusesWithEmployees.filter(r => r.status === "Rechazado").length;

        setStats({ pending, approved, rejected });
        setLoading(false);
      } catch (error) {
        console.error("Error fetching requests:", error);
        toast.error("Error al cargar las solicitudes", {
          description: "No se pudieron cargar las solicitudes. Intente nuevamente."
        });
        setLoading(false);
      }
    };

    fetchRequests();
  }, []);

  // Get status style based on status value
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

  // Handle approve request
  const handleApprove = async (id: number) => {
    try {
      // In a real implementation, you would update the status in the backend
      // await statusApi.update(id, { status: 'approved' });

      // For now, we'll just update the local state
      setRequests(prev => 
        prev.map(req => 
          req.id === id ? { ...req, status: "Aprobado" } : req
        )
      );

      // Update stats
      setStats(prev => ({
        ...prev,
        pending: prev.pending - 1,
        approved: prev.approved + 1
      }));

      toast.success("Solicitud aprobada con éxito");
    } catch (error) {
      toast.error("Error al aprobar la solicitud", {
        description: "No se pudo aprobar la solicitud. Intente nuevamente."
      });
      console.error("Error approving request:", error);
    }
  };

  // Handle reject request
  const handleReject = async (id: number) => {
    try {
      // In a real implementation, you would update the status in the backend
      // await statusApi.update(id, { status: 'rejected' });

      // For now, we'll just update the local state
      setRequests(prev => 
        prev.map(req => 
          req.id === id ? { ...req, status: "Rechazado" } : req
        )
      );

      // Update stats
      setStats(prev => ({
        ...prev,
        pending: prev.pending - 1,
        rejected: prev.rejected + 1
      }));

      toast.success("Solicitud rechazada con éxito");
    } catch (error) {
      toast.error("Error al rechazar la solicitud", {
        description: "No se pudo rechazar la solicitud. Intente nuevamente."
      });
      console.error("Error rejecting request:", error);
    }
  };

  // Calculate days between two dates
  const calculateDays = (startDate: Date, endDate: Date) => {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays + 1; // Include both start and end days
  };

  // Format date for display
  const formatDate = (date: Date) => {
    return format(new Date(date), "dd/MM/yyyy", { locale: es });
  };

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Solicitudes de Empleados</h1>
        <div className="flex gap-2">
          <Button variant="outline">
            <Filter className="mr-2 h-4 w-4" />
            Filtrar
          </Button>
          <Button>
            <Mail className="mr-2 h-4 w-4" />
            Notificar a todos
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <Card className="bg-amber-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-amber-800 font-medium mb-1">Pendientes</p>
                <h3 className="text-2xl font-bold">{stats.pending}</h3>
              </div>
              <div className="h-12 w-12 bg-amber-100 rounded-full flex items-center justify-center">
                <Clock className="h-6 w-6 text-amber-800" />
              </div>
            </div>
          </CardContent>
        </Card>
        <Card className="bg-green-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-green-800 font-medium mb-1">Aprobadas</p>
                <h3 className="text-2xl font-bold">{stats.approved}</h3>
              </div>
              <div className="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center">
                <Check className="h-6 w-6 text-green-800" />
              </div>
            </div>
          </CardContent>
        </Card>
        <Card className="bg-red-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-red-800 font-medium mb-1">Rechazadas</p>
                <h3 className="text-2xl font-bold">{stats.rejected}</h3>
              </div>
              <div className="h-12 w-12 bg-red-100 rounded-full flex items-center justify-center">
                <X className="h-6 w-6 text-red-800" />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Todas las solicitudes</CardTitle>
          <div className="flex justify-between items-center mt-2">
            <div className="text-sm text-muted-foreground">
              {loading ? (
                "Cargando solicitudes..."
              ) : (
                `Mostrando ${requests.length} solicitudes`
              )}
            </div>
            <div className="flex w-full max-w-sm items-center space-x-2">
              <Input 
                placeholder="Buscar solicitud..." 
                className="h-9" 
                type="search"
              />
              <Button size="sm" className="h-9">
                <Search size={16} />
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex justify-center py-8">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            </div>
          ) : requests.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              No hay solicitudes disponibles
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Empleado</TableHead>
                  <TableHead>Tipo</TableHead>
                  <TableHead>Fechas</TableHead>
                  <TableHead>Días</TableHead>
                  <TableHead>Estado</TableHead>
                  <TableHead>Acciones</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {requests.map(request => (
                  <TableRow key={request.id}>
                    <TableCell>
                      <div className="font-medium flex items-center">
                        <User size={16} className="mr-2 text-gray-500" />
                        {request.employee ? `${request.employee.name} ${request.employee.lastName}` : "Usuario Desconocido"}
                      </div>
                      <div className="text-xs text-muted-foreground">
                        Enviado: {request.submittedDate}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        {request.type.toLowerCase().includes("vacacion") ? (
                          <Calendar size={16} className="mr-2 text-blue-500" />
                        ) : (
                          <Clock size={16} className="mr-2 text-purple-500" />
                        )}
                        {request.type}
                      </div>
                      <div className="text-xs text-muted-foreground line-clamp-1">
                        {request.description}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div>{formatDate(request.startDate)}</div>
                      {formatDate(request.startDate) !== formatDate(request.endDate) && (
                        <div className="text-xs text-muted-foreground">
                          hasta {formatDate(request.endDate)}
                        </div>
                      )}
                    </TableCell>
                    <TableCell>{calculateDays(request.startDate, request.endDate)}</TableCell>
                    <TableCell>
                      <span className={`px-2 py-1 rounded-md text-xs ${getStatusStyle(request.status || "Pendiente")}`}>
                        {request.status || "Pendiente"}
                      </span>
                    </TableCell>
                    <TableCell>
                      {request.status === "Pendiente" ? (
                        <div className="flex space-x-2">
                          <Button 
                            variant="outline" 
                            size="sm" 
                            className="bg-green-50 hover:bg-green-100 border-green-200"
                            onClick={() => handleApprove(request.id)}
                          >
                            <Check size={14} className="mr-1 text-green-700" />
                            Aprobar
                          </Button>
                          <Button 
                            variant="outline" 
                            size="sm" 
                            className="bg-red-50 hover:bg-red-100 border-red-200"
                            onClick={() => handleReject(request.id)}
                          >
                            <X size={14} className="mr-1 text-red-700" />
                            Rechazar
                          </Button>
                        </div>
                      ) : (
                        <Button variant="ghost" size="sm">
                          Ver detalles
                        </Button>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default RequestList;
