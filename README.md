# Solution

This is my solution to the problem posed below. Is is the classic Knapsack problem of choosing an optimal set of unique item so include in a bag of fixed weight capacity. The optimal solution maximizes the value of the items packed into the bag. My reference sources for this problem were:

* The Wikipedia page describing the Knapsack problem - http://en.wikipedia.org/wiki/Knapsack_problem
* The Rosetta Code page giving sample Clojure code and test data for the problem - http://rosettacode.org/wiki/Knapsack_problem/0-1#Clojure
* The Algorithm Design Manual 2nd edition, Steven S. Skiena, Springer 2010, ISBN 978-1-84996-720-4

I have used the Rosetta Code implementation as the basis for my solution, making minor modifications to make the code more readable, removed the use of global variables, and applied a scaling technique so that knapsacks with large carrying capacities would be solvable.

Test data can be found in test/doll_smuggler/test/data/. Each test file has an attribution comment.

## Usage

* lein run
* lein run -h

Prints usage information.

    $ lein run
    usage: lein run <path to data file>


* lein run <path to test data>

Example:

    $ lein run test/doll_smuggler/test/data/p10.txt

    dolls to smuggle: (map compass water sandwich glucose banana suntan_cream trousers_h20 overclothes note_case sunglasses socks)
      total value: 1030
       max weight: 400
     total weight: 396

The data file format requires 3 columns, (name, weight, value) starting in column 1 and whitespace separated. A 4th column denoting if the item would be present in the optimal set is optional for "lein run" but required if run in the test suite. Also a line matching regex "max weight:\s+\d+" must be present to detect the knapsack capacity.
  

* lein test

The test suite runs all 11 data sets found in test/doll_smuggler/test/data.

    $ lein test
    
    Testing doll-smuggler.test.core
    
    Testing doll-smuggler.test.read.file
    
    Ran 2 tests containing 14 assertions.
    0 failures, 0 errors.

## Discussion

For my implementation of the Knapsack problem I have used
a variation of the popular Knapsack/0-1 algorithm available
at rosettacode.org. (link) The 0-1 algorithm was chosen
because each potential item is unique and non-divisible.

This algorithm makes use of dynamic programming; the
practice of trading space for time in order to avoid
recomputing values over again.

The following modifications have been made to the code:

* The items hash is passed as an argument to m instead of
  using a globall variable.

* Variable names have been changed for clarity.

* A new public method pack() has been created. It takes a
  vector of candidate structs and an integer weigth limit
  as parameters. All other methods are now private.

* According to the Algorithm Design Manual (Skiena)
  the Knapsack/0-1 algorithm is efficient for a
  knapsack size <= 1,000. Storage requirements for the
  algorithm increase linearly with knapsack size, and time
  complexity increase as a product of knapsack size
  and number if candidate iems.
  I found this to be true for sample
  data set p08.txt where the JVM ran
  out of heap space. Instead of increasing the heap
  space or dropping the data set I attempted a simple
  scaling algorithm. For pack size > 10,000 I divide
  the pack size and all item weights and round up.
  While not a perfect solution I works for all the data
  sets I had selected.


Potential Improvements

* Don't use memoization for cases where a solution is
  desiered but there is not enough space.

* Make use multiple processor cores.

* Learn and use idiomatic Clojure.

* Use ceil() instead of inc() for scaling. (rounding
  is naive but not fatal)


# Problem

You are a drug trafficker. Every day you meet with a different nice older lady (the mule) and find out how much weight she can carry in her handbag. You then meet with your supplier who has packed various drugs into a myriad of collectible porcelain dolls. Once packed with drugs, each of the precious dolls has a unique combination of weight and street value. Sometimes your supplier has more dolls than the nice lady can carry, though space in her handbag is never an issue. Your job is to choose which dolls the kind soul will carry, maximizing street value, while not going over her weight restriction.

Write a Clojure function which given:

* a set of dolls with a name and unique weight and value combination
* an overall weight restriction

Produces the optimal set of drug-packed porcelain dolls which:

* are within the total weight restriction
* maximize the total street value of drugs delivered

Include a set of executable high-level tests for your solution. The following is a set of inputs for which the correct result is known:

Input:

    max weight: 400

    available dolls:

    name    weight value
    luke        9   150
    anthony    13    35
    candice   153   200
    dorothy    50   160
    puppy      15    60
    thomas     68    45
    randal     27    60
    april      39    40
    nancy      23    30
    bonnie     52    10
    marc       11    70
    kate       32    30
    tbone      24    15
    tranny     48    10
    uma        73    40
    grumpkin   42    70
    dusty      43    75
    grumpy     22    80
    eddie       7    20
    tory       18    12
    sally       4    50
    babe       30    10

Result:

    packed dolls:

    name    weight value
    sally       4    50
    eddie       7    20
    grumpy     22    80
    dusty      43    75
    grumpkin   42    70
    marc       11    70
    randal     27    60
    puppy      15    60
    dorothy    50   160
    candice   153   200
    anthony    13    35
    luke        9   150

Hints:

* use leiningen - https://github.com/technomancy/leiningen
* read this - http://en.wikipedia.org/wiki/Knapsack_problem
* find more interesting example data for test cases on the internets


