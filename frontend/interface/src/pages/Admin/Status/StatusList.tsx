import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Plus, Pencil, Trash2, Calendar } from "lucide-react";
import { DataTable } from "@/components/DataTable";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/components/ui/sonner";
import { statusApi } from "@/utils/api";
import { format } from "date-fns";
import { es } from "date-fns/locale";

// Define Status interface based on the API
interface Status {
  id: number;
  type: string;
  startDate: Date;
  endDate: Date;
  paid: number;
  description: string;
  employeeId: number;
  statusPermissionId: number;
}

const StatusList = () => {
  const navigate = useNavigate();

  // Query to fetch statuses
  const { data: statuses = [], refetch } = useQuery({
    queryKey: ["statuses"],
    queryFn: async () => {
      const response = await statusApi.getAll();
      return response.data || [];
    },
  });

  // Handle delete status
  const handleDeleteStatus = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este estado?")) {
      try {
        await statusApi.delete(id);
        toast.success("Estado eliminado con éxito");
        refetch(); // Refresh the status list
      } catch (error) {
        toast.error("Error al eliminar el estado");
        console.error("Error deleting status:", error);
      }
    }
  };

  // Format date for display
  const formatDate = (date: Date) => {
    return format(new Date(date), "dd/MM/yyyy", { locale: es });
  };

  // Define table columns
  const columns = [
    {
      key: "type",
      header: "Tipo",
      cell: (status: Status) => (
        <div className="font-medium">{status.type}</div>
      ),
      sortable: true,
    },
    {
      key: "dates",
      header: "Fechas",
      cell: (status: Status) => (
        <div>
          <div>{formatDate(status.startDate)}</div>
          <div className="text-xs text-muted-foreground">
            hasta {formatDate(status.endDate)}
          </div>
        </div>
      ),
      sortable: true,
    },
    {
      key: "description",
      header: "Descripción",
      cell: (status: Status) => (
        <div className="max-w-sm truncate">{status.description}</div>
      ),
      sortable: false,
    },
    {
      key: "paid",
      header: "Pagado",
      cell: (status: Status) => (
        <Badge variant={status.paid ? "default" : "outline"}>
          {status.paid ? "Sí" : "No"}
        </Badge>
      ),
      sortable: true,
    },
  ];

  // Define table actions
  const actions = (status: Status) => (
    <div className="flex space-x-2">
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(`/admin/status/${status.id}/edit`)}
      >
        <Pencil className="h-4 w-4" />
        <span className="sr-only">Editar</span>
      </Button>
      <Button
        variant="ghost"
        size="sm"
        onClick={() => handleDeleteStatus(status.id)}
      >
        <Trash2 className="h-4 w-4" />
        <span className="sr-only">Eliminar</span>
      </Button>
    </div>
  );

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Gestión de Estados</h1>
        <Link to="/admin/status/new">
          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Nuevo Estado
          </Button>
        </Link>
      </div>

      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Lista de estados</CardTitle>
        </CardHeader>
        <CardContent>
          <DataTable
            data={statuses}
            columns={columns}
            actions={actions}
            searchable
            searchKeys={["type", "description"]}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default StatusList;
