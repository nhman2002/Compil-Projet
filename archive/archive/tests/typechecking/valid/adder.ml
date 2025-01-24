let rec make_adder x =
  let rec adder y = x + y + 1 in
  adder in
print_int ((make_adder 3) 7)
