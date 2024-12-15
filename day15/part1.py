from enum import Enum

class Move(Enum):
    UP = 1
    DOWN = 2
    LEFT = 3
    RIGHT = 4

def parse_movement(movement: str) -> list[Move]:
    moves = []
    for move in movement:
        match move:
            case "^":
                moves.append(Move.UP)
            case ">":
                moves.append(Move.RIGHT)
            case "<":
                moves.append(Move.LEFT)
            case "v":
                moves.append(Move.DOWN)
    return moves

class Entities(Enum):
    WALL  = "#"
    NONE  = "."
    ROBOT = "@"
    BOX = "O"

class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __add__(self, o):
        result = Vector(self.x + o.x, self.y + o.y)
        return result

VECTOR_UP    = Vector( 0, -1)
VECTOR_DOWN  = Vector( 0,  1)
VECTOR_LEFT  = Vector(-1,  0)
VECTOR_RIGHT = Vector( 1,  0)


class Map:
    def __init__(self, data: str):
        self._data = []

        for line in data.split("\n"):
            new_line = []
            for item in line:
                match item:
                    case "#":
                        new_line.append(Entities.WALL)
                    case ".":
                        new_line.append(Entities.NONE)
                    case "@":
                        self._player_pos = Vector(len(new_line), len(self._data))
                        new_line.append(Entities.ROBOT)
                    case "O":
                        new_line.append(Entities.BOX)
            self._data.append(new_line)

    def _recalculate_player_pos(self):
        for y, line in enumerate(self._data):
            for x, item in enumerate(line):
                if item == Entities.ROBOT:
                    self._player_pos = Vector(x, y)
                    return None

    def make_moves(self, moves: list[Move]):
        for move in moves:
            match move:
                case Move.UP:
                    self._move(VECTOR_UP)
                case Move.DOWN:
                    self._move(VECTOR_DOWN)
                case Move.LEFT:
                    self._move(VECTOR_LEFT)
                case Move.RIGHT:
                    self._move(VECTOR_RIGHT)

    def _move(self, direction: Vector):
        while self[self._player_pos] != Entities.ROBOT:
            self._recalculate_player_pos()

        new_pos = direction + self._player_pos
        match self[new_pos]:
            case Entities.NONE:
                self[self._player_pos] = Entities.NONE
                self[new_pos] = Entities.ROBOT
                self._player_pos = new_pos
            case Entities.BOX:
                to_move = []
                while self[new_pos] == Entities.BOX:
                    to_move.append(new_pos)
                    new_pos += direction

                if self[new_pos] == Entities.NONE:
                    for item in to_move:
                        self[item + direction] = Entities.BOX

                    self[self._player_pos] = Entities.NONE
                    self[self._player_pos + direction] = Entities.ROBOT
                    self._player_pos += direction
            case _:
                # wall
                pass

    def __getitem__(self, vec: Vector) -> Entities:
        return self._data[vec.y][vec.x]

    def __setitem__(self, vec: Vector, entity: Entities):
        self._data[vec.y][vec.x] = entity

    def __str__(self) -> str:
        result = ""
        for line in self._data:
            line_str = ""
            for item in line:
                match item:
                    case Entities.WALL:
                        line_str += "#"
                    case Entities.NONE:
                        line_str += "."
                    case Entities.ROBOT:
                        line_str += "@"
                    case Entities.BOX:
                        line_str += "O"
            result += line_str + "\n"
        return result

    def calculate_gps(self) -> int:
        result = 0
        for y, line in enumerate(self._data):
            for x, item in enumerate(line):
                if item == Entities.BOX:
                    result += (100 * y) + x
        return result


if __name__ == "__main__":
    map_file = open("input1.txt", "r")
    map_data = map_file.read()
    map_file.close()
    
    movement_file = open("input2.txt", "r")
    movement = movement_file.read()
    movement_file.close()

    moves = parse_movement(movement)
    map_obj = Map(map_data)

    map_obj.make_moves(moves)
    print(map_obj)

    gps = map_obj.calculate_gps()

    print("GPS:", gps)
