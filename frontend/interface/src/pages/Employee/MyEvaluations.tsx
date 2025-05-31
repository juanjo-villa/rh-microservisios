
import React from "react";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Award, Calendar, Star, TrendingUp } from "lucide-react";
import { useQuery } from "@tanstack/react-query";
import { performanceApi } from "@/utils/api";
import { useAuth } from "@/context/AuthContext";
import { Skeleton } from "@/components/ui/skeleton";

// Define types for our evaluation data
interface ApiEvaluation {
  id: number;
  employeeId: number;
  score: number;
  comments: string;
  date: string;
}

interface EvaluationSkills {
  technical: number;
  teamwork: number;
  communication: number;
  punctuality: number;
  productivity: number;
}

interface FormattedEvaluation {
  id: number;
  date: string;
  evaluator: string;
  score: number;
  skills: EvaluationSkills;
  strengths: string[];
  improvements: string[];
  comments: string;
}

const MyEvaluations = () => {
  const { user } = useAuth();

  // Fetch evaluations for the current user
  const { data: evaluations = [], isLoading } = useQuery({
    queryKey: ["myEvaluations", user?.id],
    queryFn: async () => {
      if (!user?.id) return [];
      const response = await performanceApi.getByEmployee(user.id);

      // Transform API data to match our component's expected format
      return response.data.map((evaluation: ApiEvaluation): FormattedEvaluation => ({
        id: evaluation.id,
        date: new Date(evaluation.date).toLocaleDateString(),
        evaluator: "Supervisor", // This might need to be fetched from another API
        score: evaluation.score,
        skills: {
          technical: evaluation.score, // These are placeholders since the API doesn't provide detailed skills
          teamwork: evaluation.score * 0.9,
          communication: evaluation.score * 0.85,
          punctuality: evaluation.score * 1.05 > 5 ? 5 : evaluation.score * 1.05,
          productivity: evaluation.score * 0.95
        },
        strengths: evaluation.comments ? 
          evaluation.comments.split('.').filter((s: string) => s.trim().length > 0).slice(0, 2) : 
          ["Buen desempeño general"],
        improvements: ["Continuar mejorando en todas las áreas"],
        comments: evaluation.comments || "No hay comentarios adicionales."
      })).sort((a: FormattedEvaluation, b: FormattedEvaluation) => 
        new Date(b.date).getTime() - new Date(a.date).getTime());
    },
    enabled: !!user?.id,
  });

  // Get last evaluation
  const lastEvaluation = evaluations.length > 0 ? evaluations[0] : null;

  // Get the next evaluation date (just a mock)
  const nextEvaluationDate = "Próximamente";

  // Function to render stars based on score
  const renderStars = (score: number) => {
    return (
      <div className="flex items-center">
        {[...Array(5)].map((_, idx) => (
          <Star 
            key={idx} 
            size={16} 
            className={idx < Math.floor(score) 
              ? "fill-yellow-400 text-yellow-400" 
              : idx < score 
                ? "fill-yellow-400 text-yellow-400 opacity-50" 
                : "text-gray-300"
            }
          />
        ))}
        <span className="ml-1 text-sm font-medium">{score.toFixed(1)}</span>
      </div>
    );
  };

  if (isLoading) {
    return (
      <div className="page-container">
        <h1 className="text-2xl font-bold mb-6">Mis Evaluaciones</h1>
        <div className="space-y-4">
          <Skeleton className="h-32 w-full" />
          <Skeleton className="h-64 w-full" />
          <Skeleton className="h-48 w-full" />
        </div>
      </div>
    );
  }

  if (evaluations.length === 0) {
    return (
      <div className="page-container">
        <h1 className="text-2xl font-bold mb-6">Mis Evaluaciones</h1>
        <Card>
          <CardContent className="pt-6">
            <div className="text-center py-8">
              <h3 className="text-xl font-medium mb-2">No tienes evaluaciones aún</h3>
              <p className="text-muted-foreground">
                Tu primera evaluación de desempeño aparecerá aquí cuando esté disponible.
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="page-container">
      <h1 className="text-2xl font-bold mb-6">Mis Evaluaciones</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <Card className="bg-blue-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-blue-800 font-medium mb-1">Calificación actual</p>
                <div className="flex items-center">
                  <h3 className="text-2xl font-bold mr-2">{lastEvaluation?.score.toFixed(1)}/5.0</h3>
                  {lastEvaluation && renderStars(lastEvaluation.score)}
                </div>
                <p className="text-xs text-muted-foreground mt-1">Última evaluación: {lastEvaluation?.date}</p>
              </div>
              <div className="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center">
                <Award className="h-6 w-6 text-blue-800" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="bg-purple-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-purple-800 font-medium mb-1">Próxima evaluación</p>
                <h3 className="text-2xl font-bold">{nextEvaluationDate}</h3>
                <p className="text-xs text-muted-foreground mt-1">Fecha estimada</p>
              </div>
              <div className="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center">
                <Calendar className="h-6 w-6 text-purple-800" />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {lastEvaluation && (
        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Última Evaluación</CardTitle>
              <CardDescription>Evaluación del {lastEvaluation.date}</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="mb-6">
                <h3 className="text-sm font-medium text-muted-foreground mb-2">Evaluador</h3>
                <p>{lastEvaluation.evaluator}</p>
              </div>

              <div className="mb-6">
                <h3 className="text-sm font-medium text-muted-foreground mb-2">Habilidades evaluadas</h3>
                <div className="space-y-3">
                  <div className="grid grid-cols-2 gap-2">
                    <div>
                      <div className="flex justify-between text-sm mb-1">
                        <span>Habilidades técnicas</span>
                        <span>{lastEvaluation.skills.technical}/5</span>
                      </div>
                      <div className="h-2 bg-gray-200 rounded-full">
                        <div 
                          className="h-2 bg-green-500 rounded-full" 
                          style={{ width: `${(lastEvaluation.skills.technical/5)*100}%` }}
                        ></div>
                      </div>
                    </div>
                    <div>
                      <div className="flex justify-between text-sm mb-1">
                        <span>Trabajo en equipo</span>
                        <span>{lastEvaluation.skills.teamwork}/5</span>
                      </div>
                      <div className="h-2 bg-gray-200 rounded-full">
                        <div 
                          className="h-2 bg-blue-500 rounded-full" 
                          style={{ width: `${(lastEvaluation.skills.teamwork/5)*100}%` }}
                        ></div>
                      </div>
                    </div>
                    <div>
                      <div className="flex justify-between text-sm mb-1">
                        <span>Comunicación</span>
                        <span>{lastEvaluation.skills.communication}/5</span>
                      </div>
                      <div className="h-2 bg-gray-200 rounded-full">
                        <div 
                          className="h-2 bg-purple-500 rounded-full" 
                          style={{ width: `${(lastEvaluation.skills.communication/5)*100}%` }}
                        ></div>
                      </div>
                    </div>
                    <div>
                      <div className="flex justify-between text-sm mb-1">
                        <span>Puntualidad</span>
                        <span>{lastEvaluation.skills.punctuality}/5</span>
                      </div>
                      <div className="h-2 bg-gray-200 rounded-full">
                        <div 
                          className="h-2 bg-amber-500 rounded-full" 
                          style={{ width: `${(lastEvaluation.skills.punctuality/5)*100}%` }}
                        ></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                <div>
                  <h3 className="text-sm font-medium text-muted-foreground mb-2">Fortalezas</h3>
                  <ul className="list-disc list-inside space-y-1">
                    {lastEvaluation.strengths.map((strength, index) => (
                      <li key={index} className="text-sm">{strength}</li>
                    ))}
                  </ul>
                </div>
                <div>
                  <h3 className="text-sm font-medium text-muted-foreground mb-2">Áreas de mejora</h3>
                  <ul className="list-disc list-inside space-y-1">
                    {lastEvaluation.improvements.map((improvement, index) => (
                      <li key={index} className="text-sm">{improvement}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <div>
                <h3 className="text-sm font-medium text-muted-foreground mb-2">Comentarios</h3>
                <p className="text-sm">{lastEvaluation.comments}</p>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Historial de evaluaciones</CardTitle>
              <CardDescription>Tu progreso a lo largo del tiempo</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {evaluations.map((evaluation) => (
                  <div 
                    key={evaluation.id}
                    className={`border p-4 rounded-lg ${evaluation.id === lastEvaluation.id ? 'border-primary bg-primary/5' : 'hover:bg-secondary/50'}`}
                  >
                    <div className="flex justify-between items-center mb-2">
                      <div className="flex items-center">
                        <Calendar size={16} className="mr-2 text-muted-foreground" />
                        <span className="text-sm font-medium">{evaluation.date}</span>
                      </div>
                      <div className="flex items-center">
                        <TrendingUp size={16} className={`mr-1 ${evaluation.score >= 4.5 ? 'text-green-600' : 'text-amber-600'}`} />
                        {renderStars(evaluation.score)}
                      </div>
                    </div>
                    <p className="text-xs text-muted-foreground">Evaluado por: {evaluation.evaluator}</p>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
};

export default MyEvaluations;
