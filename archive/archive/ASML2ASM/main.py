import sys
from ASML import Tree

def print_usage():
    print("usage: main.py [-i] [-f file] [-io file] [-fo file file]")
    print("-i            -- read ASML data from standard input, generate ASM to standard output")
    print("-f file       -- read ASML data from .asml file, generate ASM to standard output")
    print("-io file      -- read ASML data from standard input, generate ASM in the specified file")
    print("-fo file file -- read ASML data from .asml file, generate ASM in the output file")

def process_input_data(data, output_file=None):
    try:
        tree = Tree.Tree(data)
        tree.registerAllocation_Spill()
        # Uncomment the following line to debug by printing the AST
        # print(tree)
        asm_code = tree.generateAsm()
        print("hello"+asm_code)
        

        if output_file:
            with open(output_file, 'w') as file:
                file.write(asm_code)
        else:
            print(asm_code)
    except Exception as e:
        sys.exit(f"Error processing data: {e}")

def process_stdin(output_file=None):
    data = sys.stdin.read()
    process_input_data(data, output_file)

def process_file(input_file, output_file=None):
    try:
        with open(input_file, 'r') as file:
            data = file.read()
        process_input_data(data, output_file)
    except FileNotFoundError:
        sys.exit(f"Error: File '{input_file}' not found.")
    except Exception as e:
        sys.exit(f"Error reading file '{input_file}': {e}")

def main():
    if len(sys.argv) < 2:
        sys.exit("Error: Insufficient arguments. Use -h for usage information.")

    command = sys.argv[1]

    if command == '-h':
        print_usage()
    elif command == '-i':
        process_stdin()
    elif command == '-f':
        if len(sys.argv) < 3:
            sys.exit("Error: Missing input file. Use -h for usage information.")
        process_file(sys.argv[2])
    elif command == '-io':
        if len(sys.argv) < 3:
            sys.exit("Error: Missing output file. Use -h for usage information.")
        process_stdin(output_file=sys.argv[2])
    elif command == '-fo':
        if len(sys.argv) < 4:
            sys.exit("Error: Missing input or output file. Use -h for usage information.")
        process_file(sys.argv[2], output_file=sys.argv[3])
    else:
        sys.exit(f"Error: Unknown command '{command}'. Use -h for usage information.")

if __name__ == '__main__':
    main()
