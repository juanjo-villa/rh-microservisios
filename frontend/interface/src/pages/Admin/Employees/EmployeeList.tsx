
import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Plus, Pencil, UserMinus, CalendarClock } from "lucide-react";
import { DataTable } from "@/components/DataTable";
import { toast } from "@/components/ui/sonner";
import { employeeApi } from "@/utils/api";

// Define the extended Employee interface with position
interface EmployeeWithPosition {
  id: number;
  dni: string;
  name: string;
  lastname: string;
  address: string;
  email: string;
  phone: string;
  position?: {
    id: number;
    name: string;
    description: string;
    salary: number;
  };
}

const EmployeeList = () => {
  const navigate = useNavigate();

  // Query to fetch employees
  const { data: employees = [], refetch } = useQuery<EmployeeWithPosition[]>({
    queryKey: ["employees"],
    queryFn: async () => {
      const response = await employeeApi.getAll();
      return response.data || [];
    },
  });

  // Handle delete employee
  const handleDeleteEmployee = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este empleado?")) {
      try {
        await employeeApi.delete(id);
        toast.success("Empleado eliminado con éxito");
        refetch(); // Refresh the employee list
      } catch (error) {
        toast.error("Error al eliminar el empleado");
        console.error("Error deleting employee:", error);
      }
    }
  };

  // Define table columns
  const columns = [
    {
      key: "name",
      header: "Nombre",
      cell: (employee: EmployeeWithPosition) => (
        <div className="font-medium">
          {employee.name} {employee.lastname}
        </div>
      ),
      sortable: true,
    },
    {
      key: "email",
      header: "Email",
      cell: (employee: EmployeeWithPosition) => employee.email,
      sortable: true,
    },
    {
      key: "phone",
      header: "Teléfono",
      cell: (employee: EmployeeWithPosition) => employee.phone,
      sortable: false,
    },
    {
      key: "position",
      header: "Cargo",
      cell: (employee: EmployeeWithPosition) => 
        employee.position?.name || "Sin asignar",
      sortable: true,
    },
  ];

  // Define table actions
  const actions = (employee: EmployeeWithPosition) => (
    <div className="flex space-x-2">
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(`/admin/employees/${employee.id}/edit`)}
      >
        <Pencil className="h-4 w-4" />
        <span className="sr-only">Editar</span>
      </Button>
      <Button
        variant="ghost"
        size="sm"
        onClick={() => handleDeleteEmployee(employee.id)}
      >
        <UserMinus className="h-4 w-4" />
        <span className="sr-only">Eliminar</span>
      </Button>
      <Button
        variant="outline"
        size="sm"
        onClick={() => navigate(`/admin/schedules?employeeId=${employee.id}`)}
      >
        <CalendarClock className="h-4 w-4 mr-1" />
        <span className="hidden sm:inline">Horarios</span>
      </Button>
    </div>
  );

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Gestión de Empleados</h1>
        <Link to="/admin/employees/new">
          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Nuevo Empleado
          </Button>
        </Link>
      </div>

      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Lista de empleados</CardTitle>
        </CardHeader>
        <CardContent>
          <DataTable
            data={employees}
            columns={columns}
            actions={actions}
            searchable
            searchKeys={["name", "lastname", "email", "phone"]}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default EmployeeList;
