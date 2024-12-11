#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define CONTENT_SIZE 1000
#define BLINK_COUNT 75
#define DEBUG false

struct Node{
    long val;
    struct Node* next;
};

void getstones(char content[CONTENT_SIZE], struct Node* head) {
    struct Node* trav = head;
    long long stone = 0;
    for (int i = 0; i < CONTENT_SIZE && content[i] != '\0'; i++) {
        char character = content[i];

        if (character == ' ' || character == '\n') {
            trav->next = malloc(sizeof(struct Node));
            trav->next->val = stone;
            trav->next->next = NULL;
            trav = trav->next;
            stone = 0;
        } else {
            stone *= 10;
            stone += character - '0';
        }
    }
}

void printstones(struct Node* stones) {
    printf("[ ");
    for (struct Node* trav = stones; trav != NULL; trav = trav->next) {
        printf("%ld ", trav->val);
    }
    printf("]\n");
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

void secondrule(struct Node* stone) {
    long stoneval = stone->val;
    int half_size = digitcount(stoneval) / 2;
    long lower_half = 0;
    long position = 1;

    for (int i = 0; i < half_size; i++) {
        lower_half += (stoneval % 10) * position;
        stoneval /= 10;
        position *= 10;
    }

    stone->val = stoneval;

    struct Node* temp = stone->next;
    stone->next = malloc(sizeof(struct Node));
    stone->next->val = lower_half;
    stone->next->next = temp;
}

void blink(struct Node* stones) {
    struct Node* trav = stones;
    while (trav != NULL) {

#if DEBUG
        printf("In blink: ");
        printstones(trav);
#endif

        long stone = trav->val;
        if (stone == 0) {
            trav->val = 1;
        } else if (isevendigits(stone)) {
            secondrule(trav);
            trav = trav->next;
        } else {
            trav->val = stone * 2024;
        }
        trav = trav->next;
    }
}

int count(struct Node* stones) {
    int i;
    struct Node* trav = stones;
    for (i = 0; trav != NULL; i++) {
        trav = trav->next;
    }
    return i++;
}

void freestones(struct Node* stones) {
    if (stones == NULL) return;
    else {
        freestones(stones->next);
        free(stones);
    }
}

int main() {
    char content[CONTENT_SIZE];
    memset(content, '\0', CONTENT_SIZE);

    FILE *fptr;
    fptr = fopen("input.txt", "r");
    fgets(content, CONTENT_SIZE, fptr);
    fclose(fptr);


    struct Node head;
    head.val = -1;
    head.next = NULL;
    getstones(content, &head);
    struct Node* stones = head.next;

#if DEBUG
    printstones(stones);
#endif

    for (int i = 0; i < BLINK_COUNT; i++) {
        blink(stones);

#if DEBUG
        printstones(stones);
        printf("Stones %d done\n", i+1);
#endif

    }
    printf("Stones count: %d\n", count(stones));

    freestones(stones);

    return 0;
}
