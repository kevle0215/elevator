import java.util.*;

enum Direction {
    NONE,
    UP,
    DOWN
}

public class Elevator {

    private final String model;
    private final String checkedBy;

    private Date checkedDate;
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    private List<Passenger> passengers;
    private int currentFloor;
    private Direction direction;

    public Elevator(String model, String checkedBy, Date checkedDate) {
        this.model = model;
        this.checkedBy = checkedBy;

        this.checkedDate = checkedDate;
        this.upQueue = new PriorityQueue<>();
        this.downQueue = new PriorityQueue<>(Collections.reverseOrder());
        this.passengers = new ArrayList<>();
        this.currentFloor = 0;
        this.direction = Direction.NONE;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public String getModel() {
        return this.model;
    }

    public String getCheckedBy() {
        return this.checkedBy;
    }

    public Date getCheckedDate() {
        return this.checkedDate;
    }

    public void addTargetFloor(int floor) {
        // add to upqueue if above
        if (floor > currentFloor) {
            upQueue.offer(floor);
        }
        // add to downqueue if below
        else if (floor < currentFloor) {
            downQueue.offer(floor);
        } else {
            passengers.removeIf(p -> p.getDesiredFloor() == currentFloor);
        }
        // determine direction
        updateDirection();
    }

    private void updateDirection() {
        // switch directions when no more passengers going up
        if (!upQueue.isEmpty()) {
            direction = Direction.UP;
        }
        // switch direction when no more passengers going down
        else if (!downQueue.isEmpty()) {
            direction = Direction.DOWN;
        }
        // switch to NONE if no passengers
        else {
            direction = Direction.NONE;
        }
    }

    public void move() {
        while (!upQueue.isEmpty() || !downQueue.isEmpty()) {
            if (direction == Direction.UP && !upQueue.isEmpty()) {

                int nextFloor = upQueue.poll();

                // go to next floor in upqueue and remove passengers at destination
                currentFloor = nextFloor;
                removePassengers();

            } else if (direction == Direction.DOWN && !downQueue.isEmpty()) {
                int nextFloor = downQueue.poll();

                // go to next floor in downqueue and remove passengers at destination
                currentFloor = nextFloor;
                removePassengers();
            }

            // determine if either queue is empty
            updateDirection();
        }
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        addTargetFloor(passenger.getDesiredFloor());
    }

    private void removePassengers() {
        passengers.removeIf(p -> p.getDesiredFloor() == currentFloor);
    }
}

class Floor {

    private int floorNumber;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return this.floorNumber;
    }
}

class Passenger {

    private int desiredFloor;
    private int currentFloor;
    private int time;

    public Passenger(int desiredFloor, int currentFloor, int time) {
        this.desiredFloor = desiredFloor;
        this.currentFloor = currentFloor;
        this.time = time;
    }

    public int getDesiredFloor() {
        return desiredFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getTime() {
        return time;
    }
}

class Building {
    private List<Floor> floors;
    private Elevator elevator;

    public Building(int floorCount) {
        floors = new ArrayList<>();
        for (int i = 0; i < floorCount; i++) {
            floors.add(new Floor(i));
        }
        elevator = new Elevator("Model1", "Inspector", new Date());
    }

    public Floor getFloor(int floorNumber) {
        return floors.get(floorNumber);
    }

    public Elevator getElevator() {
        return elevator;
    }
}