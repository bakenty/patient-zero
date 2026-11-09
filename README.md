# Assignment 4. Patient Zero

Name: Yelubek Bakdaulet  
Student ID: 252914\
Group: [enter your group]

## Run and verify

The four files in `data` were generated for student ID `252914`.
Run these commands from the project folder with Java 17 or newer:

```sh
java tools/MakeVariant.java 252914 data
java tools/Check.java
java -cp out Main
java -cp out Bench
```

The results and examples below, along with `output/outbreak.html`, use the data for `252914`. Before submission, save a screenshot of `output/outbreak.html` as `output/outbreak.png`. Review the implementation and write the report answers in your own words, as required by the assignment.

## Program output for 252914

```
Mutations: 105
Shared core: 1214
Marker hits: 6
First hit: 2816
Border length: 3
Shared words: 459
Tests needed: 40
Cheapest testing: 184
Longest chain: 33
```

The starter checker passed all nine lines on each of `tiny`, `small`, and `dense`.

## Questions

1. **Edit distance.** The cell `d[i][j]` stores the minimum number of edits to turn the first `i` letters of A into the first `j` letters of B. It takes the minimum of deletion from above, insertion from the left, and a match or replacement from the diagonal. The distance from an empty prefix is the length of the other prefix.
2. **Shared core.** A subsequence keeps letters in order and may skip intervening letters. A substring uses consecutive letters. An insertion or deletion can shift a long matching block away from its original indices, which explains how the strains for `252914` share a core of 1,214 letters even though only 500 letters match at the same index.
3. **Marker.** For each marker prefix, the prefix function gives the length of its longest proper prefix that is also a suffix. On a mismatch KMP falls back to a shorter valid match and compares the same text character again, so the text index never moves backward. The marker `CACCGTCAC` has border length 3 because it starts and ends with `CAC`.
4. **Copied words.** Different windows can have the same hash, so character comparison verifies each candidate. Rolling the hash costs constant time, but when many windows share a hash, verifying candidates can dominate the runtime.
5. **Tree DP.** For each subtree, I compute its cheapest coverage when its root is tested and when its root is skipped. Testing a person lets each child take its cheaper state, while skipping that person forces every child to be tested. A greedy degree rule can pick an expensive center of a star even when testing all its leaves costs less.

## AI use

ChatGPT drafted the five implementations and these report answers, ran the starter checks and benchmark, and organized the draft into mission commits. It also regenerated the data and visualization for student ID `252914` and updated the report results. The starter project supplied the input reader, data generator, visualizer, benchmark, and checker.

## Timing

Milliseconds from `java -cp out Bench`, averaging three runs per size.

| Test | Input sizes | Time in ms, in order |
|---|---|---|
| Naive search | 20,000 / 40,000 / 80,000 | 13.3 / 17.5 / 35.8 |
| KMP | 20,000 / 40,000 / 80,000 | 2.6 / 0.8 / 0.6 |
| Edit distance | 500 / 1,000 / 2,000 / 4,000 | 8.1 / 10.6 / 21.5 / 77.8 |

Naive search increased by about 1.3× and 2.0× as the text doubled with a fixed marker. KMP was faster at each size, but the short timings varied too much to infer a reliable doubling factor. Edit distance grew by about 1.3×, 2.0×, and 3.6×, with the largest step closest to the fourfold growth in table cells.

## Complexity

Here `n` and `m` are the input sequence lengths, `k` is the window length, `h` is the number of KMP hits, `C` is the number of Rabin–Karp candidate pairs actually checked, and `v` is the number of people.

| Algorithm | Time | Space | Details |
|---|---|---|---|
| Edit distance | O(nm) | O(nm) | Full prefix table |
| LCS | O(nm) | O(nm) | Full prefix table |
| Prefix function and KMP | O(n + m) | O(m + h) | Includes the match positions |
| Rabin–Karp | O(n + m + kC) | O(m) | Expected constant-time hash lookups. In the worst case C can be O(nm) |
| Tree cover DP and reconstruction | O(v) | O(v) | Each edge is visited a constant number of times |
| Tree diameter | O(v) | O(v) | Tracks the two deepest child paths. The recursion stack can grow to O(v) |

## Before submission

- Add your group and screenshot.
- Verify the report and the code, then commit the updates and push `main` to a public GitHub repository.
- Open the repository link without signing in and submit the link in Moodle.
