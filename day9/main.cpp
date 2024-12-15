#include <iostream>
#include <fstream>
#include <string>
#include <vector>
using namespace std;

class DiskMap {
    vector<long int> diskMap;

public:
    DiskMap(string text) {
        for (int i = 0; i < text.size(); i++) {
            int item = text[i] - '0';

            if (i % 2 == 0) {
                for (int j = 0; j < item; j++) {
                    diskMap.push_back(i/2);
                }
            } else {
                for (int j = 0; j < item; j++) {
                    diskMap.push_back(-1);
                }
            }
        }
    }

    void compress() {
        int size = diskMap.size();
        int progress = 0;
        for (int i = size-1; i >= 0; i--) {
            long int content = diskMap[i];
            if (content == -1) continue;

            for (int j = progress; j <= i; j++) {
                if (i == j) return;

                long int secondContent = diskMap[j];
                if (secondContent != -1) continue;

                long int temp = diskMap[j];
                diskMap[j] = diskMap[i];
                diskMap[i] = temp;
                progress = j+1;
                break;
            }
        }
    }

    void advancedCompress() {
        int i = diskMap.size() - 1;
        while (i > 0) {
            long int num = diskMap[i];
            int size = 0;
            for (int j = i; j >= 0 && diskMap[j] == num; j--) {
                size++;
            }

            int begining = i - size + 1;
            for (int j = 0; j <= begining ; j++) {
                if (j == begining) break;

                long int num = diskMap[j];
                if (num != -1) continue;

                long int freespacesize = 0;
                for (int k = j; k < diskMap.size() && diskMap[k] == -1; k++) {
                    freespacesize++;
                }

                if (size <= freespacesize) {
                    for (int k = 0; k < size; k++) {
                        long int indexorig = k + begining;
                        long int indexfree = k + j;
                        long int temp = diskMap[indexfree];
                        diskMap[indexfree] = diskMap[indexorig];
                        diskMap[indexorig] = temp;
                    }
                }
            }

            i -= size;
        }
    }

    unsigned long int calculateChecksum() {
        unsigned long int result = 0;
        for (long i = 0; i < diskMap.size(); i++) {
            if (diskMap[i] == -1) continue;

            long num = diskMap[i];
            result += num * i;
        }
        return result;
    }
};

int main() {
    ifstream input("input_real.txt");
    string line;
    getline (input, line);
    input.close();

    // part 1
    // DiskMap diskMap(line);
    // diskMap.compress();
    // unsigned long int checksum = diskMap.calculateChecksum();
    // cout << "Compressed checksum: " << checksum << std::endl;
    
    // part 2
    DiskMap diskMap(line);
    diskMap.advancedCompress();
    unsigned long int checksum = diskMap.calculateChecksum();
    cout << "Compressed checksum: " << checksum << std::endl;

    return 0;
}
