#include <fstream>
#include <iostream>
#include <ostream>
#include <string>
#include <vector>

struct End {
    int x, y;
};

class Map {
    std::vector<std::string> map;
    std::vector<End> ends;

    std::string end_tostring() {
        std::string output = "[ ";
        for (int i = 0; i < ends.size(); i++) {
            output += "(";
            output += std::to_string( ends[i].x );
            output += ",";
            output += std::to_string( ends[i].y );
            output += "), ";
        }
        output.pop_back();
        output.pop_back();
        output += " ]";
        return output;
    }

    int get_trailheadscore(int x, int y, int initial_level) {
        if (initial_level == 9 && end_doesnt_exist(x, y)) {
            End newEnd;
            newEnd.x = x;
            newEnd.y = y;
            ends.push_back(newEnd);
        }

        if (y > 0 && map[y-1][x] - '0' == initial_level + 1)
            get_trailheadscore(x, y - 1, map[y-1][x] - '0');

        for (int j = x-1; j < x + 2; j++) {
            if (j < 0 || j >= map[y].size()) continue;
            int num = map[y][j] - '0';
            if (num == initial_level + 1)
                get_trailheadscore(j, y, num);
        }

        if (y < map.size()-1 && map[y+1][x] - '0' == initial_level + 1)
            get_trailheadscore(x, y+1, map[y+1][x] - '0');
        
        // if (initial_level == 0)
            // std::cout << ends.size() << end_tostring() << std::endl;
        
        return ends.size();
    }

    bool end_doesnt_exist(int x, int y) {
        for (int i = 0; i < ends.size(); i++) {
            End end = ends[i];
            if (end.y == y && end.x == x) return false;
        }
        return true;
    }

public:
    Map(std::vector<std::string> map) {
        this->map = map;
    }

    int trailhead_score() {
        int trailheadscore = 0;
        for (int y = 0; y < map.size(); y++) {
            std::string line = map[y];

            for (int x = 0; x < line.size(); x++) {
                int level = line[x] - '0';
                if (level == 0) {
                    trailheadscore += get_trailheadscore(x, y, level);
                    ends.clear();
                }
            }
        }
        return trailheadscore;
    }
};

int main() {
    std::ifstream input_file("input_real.txt");
    std::string line;
    std::vector<std::string> map_data;

    while( getline(input_file, line) ) {
        map_data.push_back(line);
    }

    Map map(map_data);

    std::cout << "Trailhead scores: " << map.trailhead_score() << std::endl;

    return 0;
}
