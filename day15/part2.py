from enum import Enum
from time import sleep

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
    BOXLEFT = "["
    BOXRIGHT = "]"

class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __add__(self, o):
        result = Vector(self.x + o.x, self.y + o.y)
        return result

    def __str__(self) -> str:
        return f"({self.x}, {self.y})"

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
                        new_line.append(Entities.WALL)
                    case ".":
                        new_line.append(Entities.NONE)
                        new_line.append(Entities.NONE)
                    case "@":
                        self._player_pos = Vector(len(new_line), len(self._data))
                        new_line.append(Entities.ROBOT)
                        new_line.append(Entities.NONE)
                    case "O":
                        new_line.append(Entities.BOXLEFT)
                        new_line.append(Entities.BOXRIGHT)
            self._data.append(new_line)

    def _recalculate_player_pos(self):
        for y, line in enumerate(self._data):
            for x, item in enumerate(line):
                if item == Entities.ROBOT:
                    self._player_pos = Vector(x, y)
                    return None

    def make_moves(self, moves: list[Move]):
        for move in moves:
            print("Move:", move, "          ")
            match move:
                case Move.UP:
                    self._move_vertical(VECTOR_UP)
                case Move.DOWN:
                    self._move_vertical(VECTOR_DOWN)
                case Move.LEFT:
                    self._move(VECTOR_LEFT)
                case Move.RIGHT:
                    self._move(VECTOR_RIGHT)
            print(self)
            # print("\033[F" * (len(self._data) + 3))
            # sleep(0.1)
            

    def _get_to_move(self, top_left, direction):
        top_right = top_left + VECTOR_RIGHT
        to_move = [top_left]

        forward_top_left = top_left + direction
        if self[forward_top_left] == Entities.BOXRIGHT:
            result = self._get_to_move(forward_top_left + VECTOR_LEFT, direction)
            if result == None:
                return None

            for move in result:
                to_move.append(move)
        elif self[forward_top_left] == Entities.BOXLEFT:
            result = self._get_to_move(forward_top_left, direction)
            if result == None:
                return None
            
            for move in result:
                to_move.append(move)

        forward_top_right = top_right + direction
        if self[forward_top_right] == Entities.BOXLEFT:
            result = self._get_to_move(forward_top_right, direction)
            if result == None:
                return None

            for move in result:
                to_move.append(move)

        if self[forward_top_left] == Entities.WALL or self[forward_top_right] == Entities.WALL:
            return None

        return to_move

    def _move_vertical(self, direction: Vector):
        while self[self._player_pos] != Entities.ROBOT:
            self._recalculate_player_pos()

        new_pos = direction + self._player_pos
        match self[new_pos]:
            case Entities.NONE:
                self[self._player_pos] = Entities.NONE
                self[new_pos] = Entities.ROBOT
                self._player_pos = new_pos
            case Entities.BOXLEFT | Entities.BOXRIGHT:
                if self[new_pos] == Entities.BOXRIGHT:
                    new_pos += VECTOR_LEFT

                to_move = self._get_to_move(new_pos, direction)
                if to_move == None:
                    # print("Hit a wall")
                    return None

                # print([str(vec) for vec in to_move])

                for box_left in to_move[::-1]:
                    box_right = box_left + VECTOR_RIGHT
                    self[box_left] = Entities.NONE
                    self[box_right] = Entities.NONE
                    self[box_left + direction] = Entities.BOXLEFT
                    self[box_right + direction] = Entities.BOXRIGHT

                self[self._player_pos] = Entities.NONE
                self[self._player_pos + direction] = Entities.ROBOT
                self._player_pos += direction
            case _:
                # wall
                pass

    def _move(self, direction: Vector):
        while self[self._player_pos] != Entities.ROBOT:
            self._recalculate_player_pos()

        new_pos = direction + self._player_pos
        match self[new_pos]:
            case Entities.NONE:
                self[self._player_pos] = Entities.NONE
                self[new_pos] = Entities.ROBOT
                self._player_pos = new_pos
            case Entities.BOXLEFT | Entities.BOXRIGHT:
                to_move = []
                while self[new_pos] == Entities.BOXLEFT or self[new_pos] == Entities.BOXRIGHT:
                    to_move.append(new_pos)
                    new_pos += direction

                if self[new_pos] == Entities.NONE:
                    for item in to_move[::-1]:
                        self[item + direction] = self[item]

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
                        line_str += " "
                    case Entities.ROBOT:
                        line_str += "@"
                    case Entities.BOXLEFT:
                        line_str += "["
                    case Entities.BOXRIGHT:
                        line_str += "]"
            result += line_str + "\n"
        return result

    def calculate_gps(self) -> int:
        result = 0
        for y, line in enumerate(self._data):
            for x, item in enumerate(line):
                if item == Entities.BOXLEFT:
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

    print(map_obj)
    try:
        map_obj.make_moves(moves)
        print("\n" * len(map_obj._data))
        print(map_obj)

        gps = map_obj.calculate_gps()
        print("GPS:", gps)
    except KeyboardInterrupt:
        print("\n" * len(map_obj._data))
