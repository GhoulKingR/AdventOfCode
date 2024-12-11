#include <stdio.h>
#include <string.h>
#include <stdbool.h>

#define CONTENT_SIZE 1000
#define STONE_COUNT 1000000
#define BLINK_COUNT 25

void getstones(char content[CONTENT_SIZE], long long stones[STONE_COUNT]) {
    long long stone = 0;
    int stonepos = 0;
    for (int i = 0; i < CONTENT_SIZE && content[i] != '\0'; i++) {
        char character = content[i];

        if (character == ' ' || character == '\n') {
            stones[stonepos] = stone;
            stonepos++;
            stone = 0;
        } else {
            stone *= 10;
            stone += character - '0';
        }
    }
}

void printstones(long long stones[STONE_COUNT]) {
    printf("[ ");
    for (int i = 0; i < STONE_COUNT && stones[i] != -1; i++) {
        printf("%lld ", stones[i]);
    }
    printf("]\n");
}

int digitcount(long long stone) {
    int count = 0;
    while (stone > 0) {
        stone /= 10;
        count++;
    }
    return count;
}

bool isevendigits(long long stone) {
    return digitcount(stone) % 2 == 0;
}

void secondrule(long pos, long long stones[STONE_COUNT]) {
    long long stone = stones[pos];
    int half_size = digitcount(stone) / 2;
    long long lower_half = 0;
    long long position = 1;

    for (int i = 0; i < half_size; i++) {
        lower_half += (stone % 10) * position;
        stone /= 10;
        position *= 10;
    }

    stones[pos] = stone;

    long long temp = lower_half;
    for (int i = pos+1; i < STONE_COUNT; i++) {
        if (temp == -1 && stones[i] == -1) break;

        long long sectemp = stones[i];
        stones[i] = temp;
        temp = sectemp;
    }
}

void blink(long long stones[STONE_COUNT]) {
    for (int i = 0; i < STONE_COUNT && stones[i] != -1; i++) {
        long stone = stones[i];
        if (stone == 0) {
            stones[i] = 1;
        } else if (isevendigits(stone)) {
            secondrule(i, stones);
            i++;
        } else {
            stones[i] = stone * 2024;
        }
    }
}

int count(long long stones[STONE_COUNT]) {
    int c = 0;
    for (int i = 0; i < STONE_COUNT && stones[i] != -1; i++) {
        c++;
    }
    return c++;
}

int main() {
    char content[CONTENT_SIZE];
    memset(content, '\0', CONTENT_SIZE);

    FILE *fptr;
    fptr = fopen("input.txt", "r");
    fgets(content, CONTENT_SIZE, fptr);
    fclose(fptr);


    long long stones[STONE_COUNT];
    memset(stones, -1, STONE_COUNT);
    getstones(content, stones);

    // printstones(stones);
    for (int i = 0; i < BLINK_COUNT; i++) {
        blink(stones);
        // printstones(stones);
        printf("Stones count: %d\n", count(stones));
    }

    return 0;
}
