let rec f x = x + 123 in
let rec d y = f in
print_int ((d 456) 789)
