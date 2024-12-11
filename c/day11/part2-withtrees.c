#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define CONTENT_SIZE 1000
#define BLINK_COUNT 75
#define MAX_LEAFS 7
#define DEBUG false

struct Leaf {
    long val;
    int leaflen;
    struct Leaf *leafs[MAX_LEAFS];
};

struct Leaf* genLeaf(long val) {
    struct Leaf* leaf = malloc(sizeof(struct Leaf));
    leaf->val = val;
    leaf->leaflen = 0;
    memset(leaf->leafs, 0, MAX_LEAFS);
    return leaf;
}

void addLeaf(struct Leaf* head, struct Leaf* leaf) {
    head->leafs[head->leaflen] = leaf;
    head->leaflen++;
}

void getstones(char content[CONTENT_SIZE], struct Leaf* head) {
    long stone = 0;
    for (int i = 0; i < CONTENT_SIZE && content[i] != '\0'; i++) {
        char character = content[i];

        if (character == ' ' || character == '\n') {
            addLeaf(head, genLeaf(stone));
            stone = 0;
        } else {
            stone *= 10;
            stone += character - '0';
        }
    }
}

int digitcount(long stone) {
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

void printleaf(struct Leaf* head) {
    printf("Leaf %ld: ( ", head->val);
    for (int i = 0; i < head->leaflen; i++) {
        printf("%ld ", head->leafs[i]->val);
    }
    printf(")\n");
}


void secondrule(struct Leaf* head) {
    long stoneval = head->val;
    int half_size = digitcount(stoneval) / 2;
    long lower_half = 0;
    long position = 1;

    for (int i = 0; i < half_size; i++) {
        lower_half += (stoneval % 10) * position;
        stoneval /= 10;
        position *= 10;
    }

    addLeaf(head, genLeaf(stoneval));
    addLeaf(head, genLeaf(lower_half));
}

void blink(struct Leaf* head, int count, long* total) {
    if (count <= -1) {
        (*total)++;
        return;
    }

    long stone = head->val;
    if (stone == 0) {
        addLeaf(head, genLeaf(1));
    } else if (stone > 0 && isevendigits(stone)) {
        secondrule(head);
    } else if(stone > 0) {
        addLeaf(head, genLeaf(stone * 2024));
    }

    // printleaf(head);
    // printf("node %ld, blink %d\n", stone, count);

    for (int i = 0; i < head->leaflen; i++) {
        blink(head->leafs[i], count-1, total);
        head->leafs[i] = NULL;
    }

    free(head);
}

int main() {
    char content[CONTENT_SIZE];
    memset(content, '\0', CONTENT_SIZE);

    FILE *fptr;
    fptr = fopen("input.txt", "r");
    fgets(content, CONTENT_SIZE, fptr);
    fclose(fptr);

    struct Leaf* head = genLeaf(-1);
    getstones(content, head);

#if DEBUG
    printstones(stones);
#endif

    long total = 0;
    blink(head, BLINK_COUNT, &total);

#if DEBUG
    printstones(stones);
    printf("Stones %d done\n", i+1);
#endif

    printf("Stones count: %ld\n", total);

    return 0;
}
