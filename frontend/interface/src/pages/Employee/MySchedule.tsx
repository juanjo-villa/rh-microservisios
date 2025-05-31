
import React, { useState, useEffect } from "react";
import { scheduleApi } from "@/utils/api";
import { Card, CardContent, CardDescription, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Clock, Calendar, Play, StopCircle } from "lucide-react";
import { toast } from "@/components/ui/sonner";

// Define Schedule type
interface Schedule {
  id: number;
  date: string;
  startTime: string;
  endTime: string;
  location: string;
  status: string;
  shiftType: string;
}

// Define TimeEntry type for tracking work time
interface TimeEntry {
  id: number;
  date: string;
  startTime: string;
  endTime: string | null;
  duration: number | null;
}

const MySchedule: React.FC = () => {
  const [schedules, setSchedules] = useState<Schedule[]>([]);
  const [timeEntries, setTimeEntries] = useState<TimeEntry[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [weekStartDate, setWeekStartDate] = useState<Date>(new Date());
  const [activeTimeEntry, setActiveTimeEntry] = useState<TimeEntry | null>(null);
  const [timer, setTimer] = useState<number>(0);
  const [isRunning, setIsRunning] = useState<boolean>(false);

  // Helper to format date
  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
  };

  // Helper to format time
  const formatTime = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  // Helper to get current week dates
  const getWeekDates = (startDate: Date) => {
    const dates = [];
    for (let i = 0; i < 7; i++) {
      const date = new Date(startDate);
      date.setDate(date.getDate() + i);
      dates.push(date);
    }
    return dates;
  };

  // Load stored time entries from localStorage on component mount
  useEffect(() => {
    const storedEntries = localStorage.getItem('timeEntries');
    if (storedEntries) {
      setTimeEntries(JSON.parse(storedEntries));
    }

    const activeEntry = localStorage.getItem('activeTimeEntry');
    if (activeEntry) {
      const entry = JSON.parse(activeEntry);
      setActiveTimeEntry(entry);
      setIsRunning(true);

      // Calculate elapsed time
      const startTime = new Date(entry.startTime).getTime();
      const elapsed = Math.floor((Date.now() - startTime) / 1000);
      setTimer(elapsed);
    }
  }, []);

  // Timer effect
  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;

    if (isRunning) {
      interval = setInterval(() => {
        setTimer(prevTime => prevTime + 1);
      }, 1000);
    } else if (interval) {
      clearInterval(interval);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isRunning]);

  // Fetch schedules data
  useEffect(() => {
    const fetchSchedules = async () => {
      try {
        setLoading(true);

        // Get current user from localStorage
        const userJson = localStorage.getItem('user');
        if (!userJson) {
          setLoading(false);
          return;
        }

        const user = JSON.parse(userJson);
        const employeeId = user.id;

        // Set weekStartDate to beginning of current week (Sunday)
        const today = new Date();
        const diff = today.getDate() - today.getDay();
        const firstDayOfWeek = new Date(today.setDate(diff));
        setWeekStartDate(firstDayOfWeek);

        // Fetch employee schedules
        const response = await scheduleApi.getAllEmployeeSchedules();
        const employeeSchedules = response.data.filter(es => es.employeeId === employeeId);

        // Fetch schedule details for each employee schedule
        const schedulePromises = employeeSchedules.map(es => 
          scheduleApi.getById(es.scheduleId)
        );

        const scheduleResponses = await Promise.all(schedulePromises);
        const fetchedSchedules = scheduleResponses.map(response => {
          const schedule = response.data;
          return {
            id: schedule.id,
            date: schedule.date,
            startTime: schedule.startTime,
            endTime: schedule.exitTime,
            location: "Office", // Default location
            status: "scheduled",
            shiftType: schedule.totalHours <= 4 ? "half-day" : "regular"
          };
        });

        setSchedules(fetchedSchedules);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching schedules:", error);
        setLoading(false);

        // Fallback to mock data if API fails
        const today = new Date();
        const diff = today.getDate() - today.getDay();
        const firstDayOfWeek = new Date(today.setDate(diff));

        const mockSchedules = [
          {
            id: 1,
            date: new Date(firstDayOfWeek.getTime() + 1 * 24 * 60 * 60 * 1000).toISOString().split('T')[0], // Monday
            startTime: "09:00",
            endTime: "17:00",
            location: "Main Office",
            status: "scheduled",
            shiftType: "regular"
          },
          {
            id: 2,
            date: new Date(firstDayOfWeek.getTime() + 2 * 24 * 60 * 60 * 1000).toISOString().split('T')[0], // Tuesday
            startTime: "09:00",
            endTime: "17:00",
            location: "Main Office",
            status: "scheduled",
            shiftType: "regular"
          }
        ];

        setSchedules(mockSchedules);
      }
    };

    fetchSchedules();
  }, []);

  // Get current week dates
  const weekDates = getWeekDates(weekStartDate);

  // Helper to get schedule for a specific date
  const getScheduleForDate = (date: Date) => {
    const dateStr = date.toISOString().split('T')[0];
    return schedules.find(schedule => schedule.date === dateStr);
  };

  // Navigate to previous week
  const goPreviousWeek = () => {
    const newStartDate = new Date(weekStartDate);
    newStartDate.setDate(newStartDate.getDate() - 7);
    setWeekStartDate(newStartDate);
  };

  // Navigate to next week
  const goNextWeek = () => {
    const newStartDate = new Date(weekStartDate);
    newStartDate.setDate(newStartDate.getDate() + 7);
    setWeekStartDate(newStartDate);
  };

  // Start time tracking
  const startTimeTracking = () => {
    const now = new Date();
    const todayStr = now.toISOString().split('T')[0];

    const newEntry: TimeEntry = {
      id: Date.now(),
      date: todayStr,
      startTime: now.toISOString(),
      endTime: null,
      duration: null
    };

    setActiveTimeEntry(newEntry);
    setIsRunning(true);
    setTimer(0);

    localStorage.setItem('activeTimeEntry', JSON.stringify(newEntry));
    toast.success("Time tracking started", {
      description: `Started at ${now.toLocaleTimeString()}`
    });
  };

  // Stop time tracking
  const stopTimeTracking = async () => {
    if (activeTimeEntry) {
      const now = new Date();
      const endedEntry: TimeEntry = {
        ...activeTimeEntry,
        endTime: now.toISOString(),
        duration: timer
      };

      // Add to time entries list
      const updatedEntries = [...timeEntries, endedEntry];
      setTimeEntries(updatedEntries);

      // Store in localStorage
      localStorage.setItem('timeEntries', JSON.stringify(updatedEntries));
      localStorage.removeItem('activeTimeEntry');

      setActiveTimeEntry(null);
      setIsRunning(false);

      // Calculate hours worked (convert seconds to hours)
      const hoursWorked = timer / 3600;

      try {
        // Get current user from localStorage
        const userJson = localStorage.getItem('user');
        if (!userJson) {
          toast.error("User information not found");
          return;
        }

        const user = JSON.parse(userJson);
        const employeeId = user.id;

        // Register hours in the backend
        await scheduleApi.registerHours(employeeId, hoursWorked);

        toast.success("Time tracking stopped", {
          description: `Total time: ${formatTime(timer)}`
        });
      } catch (error) {
        console.error("Error registering hours:", error);
        toast.error("Error registering hours", {
          description: "Your hours were saved locally but could not be sent to the server."
        });
      }
    }
  };

  // Calculate total hours worked today
  const calculateTodaysHours = () => {
    const todayStr = new Date().toISOString().split('T')[0];
    const todaysEntries = timeEntries.filter(entry => entry.date === todayStr);

    const totalSeconds = todaysEntries.reduce((total, entry) => {
      return total + (entry.duration || 0);
    }, 0);

    return formatTime(totalSeconds);
  };

  // Calculate total hours worked this week
  const calculateWeekHours = () => {
    const weekEntries = timeEntries.filter(entry => {
      const entryDate = new Date(entry.date);
      const weekStart = new Date(weekStartDate);
      const weekEnd = new Date(weekStartDate);
      weekEnd.setDate(weekEnd.getDate() + 6);

      return entryDate >= weekStart && entryDate <= weekEnd;
    });

    const totalSeconds = weekEntries.reduce((total, entry) => {
      return total + (entry.duration || 0);
    }, 0);

    return formatTime(totalSeconds);
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>My Schedule</h1>
      </div>

      <div className="space-y-6">
        {/* Time tracking card */}
        <Card>
          <CardHeader className="pb-3">
            <CardTitle>Time Tracking</CardTitle>
            <CardDescription>Track your working hours</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
              <div className="flex items-center gap-2 rounded-lg border p-3">
                <div className="rounded-full bg-primary/10 p-2">
                  <Clock className="h-4 w-4 text-primary" />
                </div>
                <div>
                  <p className="text-sm font-medium">Today</p>
                  <p className="text-xl font-bold">{calculateTodaysHours()}</p>
                </div>
              </div>
              <div className="flex items-center gap-2 rounded-lg border p-3">
                <div className="rounded-full bg-primary/10 p-2">
                  <Calendar className="h-4 w-4 text-primary" />
                </div>
                <div>
                  <p className="text-sm font-medium">This week</p>
                  <p className="text-xl font-bold">{calculateWeekHours()}</p>
                </div>
              </div>
              <div className="sm:col-span-2 flex items-center gap-2 rounded-lg border p-3">
                {isRunning ? (
                  <div className="w-full">
                    <div className="mb-2 flex items-center justify-between">
                      <span className="text-sm font-medium">Currently working</span>
                      <Badge className="bg-green-100 text-green-800">Active</Badge>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-xl font-bold">{formatTime(timer)}</span>
                      <Button 
                        variant="destructive"
                        onClick={stopTimeTracking}
                        className="ml-4"
                      >
                        <StopCircle className="mr-2 h-4 w-4" />
                        Stop Working
                      </Button>
                    </div>
                  </div>
                ) : (
                  <div className="w-full">
                    <div className="mb-2 flex items-center justify-between">
                      <span className="text-sm font-medium">Start tracking</span>
                      <Badge variant="outline">Inactive</Badge>
                    </div>
                    <Button 
                      variant="default" 
                      onClick={startTimeTracking} 
                      className="w-full"
                    >
                      <Play className="mr-2 h-4 w-4" />
                      Start Working
                    </Button>
                  </div>
                )}
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Quick summary card */}
        <Card>
          <CardHeader className="pb-3">
            <CardTitle>Weekly Overview</CardTitle>
            <CardDescription>Your work schedule for the current period</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-4">
              <div className="flex items-center gap-2 rounded-lg border p-3">
                <div className="rounded-full bg-primary/10 p-2">
                  <Clock className="h-4 w-4 text-primary" />
                </div>
                <div>
                  <p className="text-sm font-medium">Hours this week</p>
                  <p className="text-xl font-bold">36</p>
                </div>
              </div>
              <div className="flex items-center gap-2 rounded-lg border p-3">
                <div className="rounded-full bg-primary/10 p-2">
                  <Calendar className="h-4 w-4 text-primary" />
                </div>
                <div>
                  <p className="text-sm font-medium">Scheduled Days</p>
                  <p className="text-xl font-bold">5</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Calendar view */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <div>
              <CardTitle>Schedule Calendar</CardTitle>
              <CardDescription>
                {weekStartDate.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })} -
                {' '}
                {new Date(weekStartDate.getTime() + 6 * 24 * 60 * 60 * 1000).toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })}
              </CardDescription>
            </div>
            <div className="flex gap-1">
              <button 
                onClick={goPreviousWeek}
                className="p-2 rounded-md hover:bg-muted"
              >
                ←
              </button>
              <button 
                className="p-2 rounded-md hover:bg-muted"
                onClick={() => setWeekStartDate(new Date(new Date().setDate(new Date().getDate() - new Date().getDay())))}
              >
                Today
              </button>
              <button 
                onClick={goNextWeek}
                className="p-2 rounded-md hover:bg-muted"
              >
                →
              </button>
            </div>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="w-full flex justify-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
              </div>
            ) : (
              <div className="grid grid-cols-7 gap-2">
                {weekDates.map((date, index) => {
                  const schedule = getScheduleForDate(date);
                  const isToday = new Date().toDateString() === date.toDateString();

                  return (
                    <div 
                      key={index} 
                      className={`border rounded-md p-3 min-h-[100px] ${
                        isToday ? "bg-primary/5 border-primary/20" : ""
                      }`}
                    >
                      <div className="text-sm font-medium mb-1">
                        {date.toLocaleDateString('en-US', { weekday: 'short' })}
                      </div>
                      <div className="text-sm text-muted-foreground mb-2">
                        {date.toLocaleDateString('en-US', { day: 'numeric' })}
                      </div>

                      {schedule ? (
                        <div className="space-y-2">
                          <Badge className={
                            schedule.shiftType === "half-day" 
                              ? "bg-blue-100 text-blue-800" 
                              : "bg-green-100 text-green-800"
                          }>
                            {schedule.shiftType === "half-day" ? "Half Day" : "Full Day"}
                          </Badge>
                          <div className="text-xs">
                            <div className="font-medium">{schedule.startTime} - {schedule.endTime}</div>
                            <div className="text-muted-foreground">{schedule.location}</div>
                          </div>
                        </div>
                      ) : (
                        <div className="text-xs text-muted-foreground italic">No shifts</div>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </CardContent>
        </Card>

        {/* Time entries list */}
        <Card>
          <CardHeader>
            <CardTitle>Recent Time Entries</CardTitle>
          </CardHeader>
          <CardContent>
            {timeEntries.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">
                No time entries recorded yet
              </p>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Date</TableHead>
                    <TableHead>Start Time</TableHead>
                    <TableHead>End Time</TableHead>
                    <TableHead>Duration</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {[...timeEntries].reverse().slice(0, 5).map((entry) => (
                    <TableRow key={entry.id}>
                      <TableCell>{formatDate(entry.date)}</TableCell>
                      <TableCell>{new Date(entry.startTime).toLocaleTimeString()}</TableCell>
                      <TableCell>{entry.endTime ? new Date(entry.endTime).toLocaleTimeString() : "-"}</TableCell>
                      <TableCell>{entry.duration ? formatTime(entry.duration) : "-"}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </CardContent>
        </Card>

        {/* Schedule list */}
        <Card>
          <CardHeader>
            <CardTitle>Upcoming Shifts</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="w-full flex justify-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
              </div>
            ) : schedules.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">
                No upcoming shifts scheduled
              </p>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Date</TableHead>
                    <TableHead>Time</TableHead>
                    <TableHead>Location</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {schedules.map((schedule) => (
                    <TableRow key={schedule.id}>
                      <TableCell>{formatDate(schedule.date)}</TableCell>
                      <TableCell>{schedule.startTime} - {schedule.endTime}</TableCell>
                      <TableCell>{schedule.location}</TableCell>
                      <TableCell>
                        <Badge className={
                          schedule.shiftType === "half-day" 
                            ? "bg-blue-100 text-blue-800" 
                            : "bg-green-100 text-green-800"
                        }>
                          {schedule.shiftType === "half-day" ? "Half Day" : "Full Day"}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge className="bg-green-100 text-green-800">
                          Scheduled
                        </Badge>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default MySchedule;
