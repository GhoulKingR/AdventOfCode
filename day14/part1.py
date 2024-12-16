class Vec:
    def __init__(self, data) -> None:
        nums = data.split(",")
        self.x = int(nums[0])
        self.y = int(nums[1])

    def __str__(self):
        return f"({self.x}, {self.y})"

    def __add__(self, o):
        return Vec(f"{self.x + o.x},{self.y + o.y}")

    def __sub__(self, o):
        return Vec(f"{self.x - o.x},{self.y - o.y}")

    def __mod__(self, o):
        return Vec(f"{self.x % o.x},{self.y % o.y}")
    
    def __floordiv__(self, s):
        return Vec(f"{self.x // s},{self.y // s}")

def get_robots():
    file = open("input.txt", "r")
    data = file.read().strip()
    file.close()
    robots = []
    for line in data.split("\n"):
        p, v = line.split(" ")
        robots.append({ "pos": Vec(p[2:]), "vel": Vec(v[2:]) })
    return robots

def move_all(robots, dimensions):
    for r in robots:
        r['pos'] += r['vel']
        r['pos'] %= dimensions

def display(robots):
    print("Robots: ")
    for r in robots:
        print(f"\t p: {r['pos']}, v: {r['vel']}")

def display_safety_factor(robots, dimensions):
    center = dimensions // 2
    counts = [0, 0, 0, 0]
    for r in robots:
        p = r['pos']
        if p.y < center.y and p.x < center.x:
            counts[0] += 1
        elif p.y < center.y and p.x > center.x:
            counts[1] += 1
        elif p.y > center.y and p.x < center.x:
            counts[2] += 1
        elif p.y > center.y and p.x > center.x:
            counts[3] += 1

    print(counts)
    total = 1
    for c in counts:
        total *= c
    print("safety factor:", total)


if __name__ == "__main__":
    robots = get_robots()
    dimensions = Vec("101,103")
    for _ in range(100):
        move_all(robots, dimensions)
    display_safety_factor(robots, dimensions)
