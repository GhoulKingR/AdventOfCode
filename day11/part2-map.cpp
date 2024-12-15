#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <unordered_map>
#include <utility>

std::unordered_map<long long, long long> getstones(std::string line) {
    std::unordered_map<long long, long long> stones;
    std::stringstream s(line);
    std::string num;
    while (s >> num) {
        long inum = std::stoll(num);
        if (stones.find(inum) == stones.end()) {
            stones[inum] = 1;
        } else {
            stones[inum]++;
        }
    }
    return stones;
}

int digitcount(long long stone) {
    int count = 0;
    while (stone > 0) {
        stone /= 10;
        count++;
    }
    return count;
}

bool isevendigits(long stone) {
    return digitcount(stone) % 2 == 0;
}

long long getordef(std::unordered_map<long long, long long> res, long key, long def) {
    if (res.find(key) == res.end()) {
        return def;
    } else {
        return res[key];
    }
}

std::unordered_map<long long, long long> blink(std::unordered_map<long long, long long> stones) {
    std::unordered_map<long long, long long> result;
    for (std::pair<long long, long long> kv: stones) {
        long long stone = kv.first;
        long long freq = kv.second;

        if (stone == 0) {
            result[1] = freq;
        } else if (isevendigits(stone)) {
            int half_size = digitcount(stone) / 2;
            long long lower_half = 0;
            long long upper_half = stone;
            long long position = 1;

            for (int i = 0; i < half_size; i++) {
                lower_half += (upper_half % 10) * position;
                upper_half /= 10;
                position *= 10;
            }

            result[upper_half] = getordef(result, upper_half, 0) + freq;
            result[lower_half] = getordef(result, lower_half, 0) + freq;
        } else {
            long long num = stone * 2024;
            result[num] = getordef(result, num, 0) + freq;
        }
    }
    return result;
}

long long count(std::unordered_map<long long, long long> stones) {
    long long count = 0;
    for (auto kv: stones) {
        count += kv.second;
    }
    return count;
}

void print(std::unordered_map<long long, long long> stones) {
    std::cout << "Map {\n";
    for (auto kv: stones) {
        std::cout << "\t" << kv.first << " -> " << kv.second << "\n";
    }
    std::cout << "}\n";
}


int main() {
    std::ifstream file("test2.txt");
    std::string line;
    std::getline(file, line);
    std::unordered_map<long long, long long> stones = getstones(line);
    // blink once
    int numblinks = 25;
    for (int i = 0; i < numblinks; i++) {
        stones = blink(stones);
    }
    print(stones);
    std::cout << "Num of stones: " << count(stones) << std::endl;
    return 0;
}

