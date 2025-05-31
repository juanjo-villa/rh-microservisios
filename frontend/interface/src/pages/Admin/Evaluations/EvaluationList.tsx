
import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { DataTable } from "@/components/DataTable";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/components/ui/sonner";
import { Plus, FilePen, FileX, Star } from "lucide-react";
import { evaluationApi } from "@/utils/api";

const EvaluationList = () => {
  const navigate = useNavigate();
  
  // Query to fetch evaluations
  const { data: evaluations = [], refetch } = useQuery({
    queryKey: ["evaluations"],
    queryFn: async () => {
      const response = await evaluationApi.getAll();
      return response.data || [
        // Mock data if API is not working
        { 
          id: 1, 
          employee: { id: 1, name: "Juan", lastname: "Pérez" },
          date: "2025-04-15", 
          score: 4.5,
          comments: "Excelente desempeño en el último trimestre"
        },
        { 
          id: 2, 
          employee: { id: 2, name: "María", lastname: "García" }, 
          date: "2025-04-10", 
          score: 3.8,
          comments: "Buen desempeño, pero puede mejorar en trabajo en equipo"
        },
        { 
          id: 3, 
          employee: { id: 3, name: "Carlos", lastname: "Rodríguez" }, 
          date: "2025-04-05", 
          score: 2.5,
          comments: "Necesita mejorar en puntualidad y cumplimiento de tareas"
        },
      ];
    },
  });

  // Handle delete evaluation
  const handleDeleteEvaluation = async (id: number) => {
    if (confirm("¿Está seguro de eliminar esta evaluación?")) {
      try {
        await evaluationApi.delete(id);
        toast.success("Evaluación eliminada con éxito");
        refetch();
      } catch (error) {
        toast.error("Error al eliminar la evaluación");
        console.error("Error deleting evaluation:", error);
      }
    }
  };

  // Format date
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-ES').format(date);
  };
  
  // Get score color based on value
  const getScoreColor = (score: number) => {
    if (score >= 4.5) return "bg-green-100 text-green-800";
    if (score >= 3.5) return "bg-blue-100 text-blue-800";
    if (score >= 2.5) return "bg-amber-100 text-amber-800";
    return "bg-red-100 text-red-800";
  };

  // Get score label
  const getScoreLabel = (score: number) => {
    if (score >= 4.5) return "Excelente";
    if (score >= 3.5) return "Muy Bueno";
    if (score >= 2.5) return "Bueno";
    if (score >= 1.5) return "Regular";
    return "Insuficiente";
  };
  
  // Render stars for score
  const renderScoreStars = (score: number) => {
    const fullStars = Math.floor(score);
    const hasHalfStar = score - fullStars >= 0.5;
    const totalStars = 5;
    
    return (
      <div className="flex items-center">
        {[...Array(fullStars)].map((_, i) => (
          <Star key={i} size={16} className="fill-yellow-400 text-yellow-400" />
        ))}
        {hasHalfStar && (
          <div className="relative">
            <Star size={16} className="text-yellow-400" />
            <div className="absolute top-0 left-0 w-1/2 overflow-hidden">
              <Star size={16} className="fill-yellow-400 text-yellow-400" />
            </div>
          </div>
        )}
        {[...Array(totalStars - fullStars - (hasHalfStar ? 1 : 0))].map((_, i) => (
          <Star key={i + fullStars + (hasHalfStar ? 1 : 0)} size={16} className="text-gray-300" />
        ))}
        <span className="ml-2 text-sm font-medium">{score.toFixed(1)}</span>
      </div>
    );
  };
  
  // Define table columns
  const columns = [
    {
      key: "employee",
      header: "Empleado",
      cell: (evaluation: any) => (
        <div className="font-medium">
          {evaluation.employee?.name} {evaluation.employee?.lastname}
        </div>
      ),
      sortable: true,
    },
    {
      key: "date",
      header: "Fecha",
      cell: (evaluation: any) => formatDate(evaluation.date),
      sortable: true,
    },
    {
      key: "score",
      header: "Puntuación",
      cell: (evaluation: any) => renderScoreStars(evaluation.score),
      sortable: true,
    },
    {
      key: "scoreLabel",
      header: "Calificación",
      cell: (evaluation: any) => (
        <Badge variant="outline" className={getScoreColor(evaluation.score)}>
          {getScoreLabel(evaluation.score)}
        </Badge>
      ),
      sortable: false,
    },
    {
      key: "comments",
      header: "Comentarios",
      cell: (evaluation: any) => (
        <div className="max-w-sm truncate">{evaluation.comments}</div>
      ),
      sortable: false,
    },
  ];
  
  // Define table actions
  const actions = (evaluation: any) => (
    <div className="flex space-x-2">
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(`/admin/evaluations/${evaluation.id}/edit`)}
      >
        <FilePen size={16} />
        <span className="sr-only">Editar</span>
      </Button>
      <Button
        variant="ghost"
        size="sm"
        onClick={() => handleDeleteEvaluation(evaluation.id)}
      >
        <FileX size={16} />
        <span className="sr-only">Eliminar</span>
      </Button>
    </div>
  );

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Evaluaciones de Desempeño</h1>
        <Link to="/admin/evaluations/new">
          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Nueva Evaluación
          </Button>
        </Link>
      </div>
      
      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Historial de Evaluaciones</CardTitle>
        </CardHeader>
        <CardContent>
          <DataTable
            data={evaluations}
            columns={columns}
            actions={actions}
            searchable
            searchKeys={["employee.name", "employee.lastname", "comments"]}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default EvaluationList;
