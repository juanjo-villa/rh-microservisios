
import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Plus, Pencil, Trash2 } from "lucide-react";
import { DataTable } from "@/components/DataTable";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/components/ui/sonner";
import { roleApi } from "@/utils/api";

const RoleList = () => {
  const navigate = useNavigate();
  
  // Query to fetch roles
  const { data: roles = [], refetch } = useQuery({
    queryKey: ["roles"],
    queryFn: async () => {
      const response = await roleApi.getAll();
      return response.data || [];
    },
  });

  // Handle delete role
  const handleDeleteRole = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este cargo?")) {
      try {
        await roleApi.delete(id);
        toast.success("Cargo eliminado con éxito");
        refetch(); // Refresh the role list
      } catch (error) {
        toast.error("Error al eliminar el cargo");
        console.error("Error deleting role:", error);
      }
    }
  };
  
  // Format currency
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-ES', {
      style: 'currency',
      currency: 'EUR',
      minimumFractionDigits: 2
    }).format(amount);
  };
  
  // Define table columns
  const columns = [
    {
      key: "name",
      header: "Nombre",
      cell: (role: any) => (
        <div className="font-medium">{role.name}</div>
      ),
      sortable: true,
    },
    {
      key: "description",
      header: "Descripción",
      cell: (role: any) => (
        <div className="max-w-sm truncate">{role.description}</div>
      ),
      sortable: false,
    },
    {
      key: "salary",
      header: "Salario Base",
      cell: (role: any) => formatCurrency(role.salary),
      sortable: true,
    },
    {
      key: "employees",
      header: "Empleados",
      cell: (role: any) => (
        <Badge variant="secondary">{role.employeeCount || 0}</Badge>
      ),
      sortable: true,
    },
  ];
  
  // Define table actions
  const actions = (role: any) => (
    <div className="flex space-x-2">
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(`/admin/roles/${role.id}/edit`)}
      >
        <Pencil className="h-4 w-4" />
        <span className="sr-only">Editar</span>
      </Button>
      <Button
        variant="ghost"
        size="sm"
        onClick={() => handleDeleteRole(role.id)}
      >
        <Trash2 className="h-4 w-4" />
        <span className="sr-only">Eliminar</span>
      </Button>
    </div>
  );

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Gestión de Cargos</h1>
        <Link to="/admin/roles/new">
          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Nuevo Cargo
          </Button>
        </Link>
      </div>
      
      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Lista de cargos</CardTitle>
        </CardHeader>
        <CardContent>
          <DataTable
            data={roles}
            columns={columns}
            actions={actions}
            searchable
            searchKeys={["name", "description"]}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default RoleList;
