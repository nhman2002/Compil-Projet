import * as fs from "fs";
import * as readline from "readline";
import { Tree } from "./ASML"; // Adjust according to the actual module structure

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
});


function main() {
    if (process.argv.length < 3) {
        console.error("Insufficient arguments");
        process.exit(1);
    }

    const option = process.argv[2];

    switch (option) {
        case "-h":
            printUsage();
            break;
        case "-i":
            readFromStdin();
            break;
        case "-f":
            if (process.argv.length < 4) {
                console.error("Insufficient arguments");
                process.exit(1);
            } else {
                readFromFile(process.argv[3]);
            }
            break;
        case "-io":
            if (process.argv.length < 4) {
                console.error("Insufficient arguments");
                process.exit(1);
            } else {
                writeToFileFromStdin(process.argv[3]);
            }
            break;
        case "-fo":
            if (process.argv.length < 5) {
                console.error("Insufficient arguments");
                process.exit(1);
            } else {
                writeToFileFromFile(process.argv[3], process.argv[4]);
            }
            break;
        default:
            console.error("Unknown option: " + option);
            process.exit(1);
    }
}

function printUsage() {
    console.log("usage: main.js [-i] [-f file] [-io file] [-fo file]");
    console.log(
        "-i            -- read asml data from standard input, generate asm to standard output"
    );
    console.log(
        "-f file       -- read asml data from .asml file, generate asm to standard output"
    );
    console.log(
        "-io file      -- read asml data from standard input, generate asm in argument file"
    );
    console.log(
        "-fo file file -- read asml .asml file, generate asm to outputFile"
    );
}

function readFromStdin() {
    let data = "";
    rl.on("line", (line) => {
        data += line + "\n";
    });

    rl.on("close", () => {
        const t = new Tree(data);
        t.registerAllocation_Spill();
        console.log(t.generateAsm());
    });
}

function readFromFile(filePath: string) {
    fs.readFile(filePath, "utf8", (err, data) => {
        if (err) {
            console.error("Error reading file:", err);
            process.exit(1);
        }
        const t = new Tree(data);
        t.registerAllocation_Spill();
        console.log(t.generateAsm());
    });
}

function writeToFileFromStdin(outputFile: string) {
    let data = "";
    rl.on("line", (line) => {
        data += line + "\n";
    });

    rl.on("close", () => {
        const t = new Tree(data);
        t.registerAllocation_Spill();
        fs.writeFile(outputFile, t.generateAsm(), (err) => {
            if (err) {
                console.error("Error writing to file:", err);
                process.exit(1);
            }
            console.log(`ASM written to ${outputFile}`);
        });
    });
}

function writeToFileFromFile(inputFile: string, outputFile: string) {
    fs.readFile(inputFile, "utf8", (err, data) => {
        if (err) {
            console.error("Error reading file:", err);
            process.exit(1);
        }
        const t = new Tree(data);
        t.registerAllocation_Spill();
        fs.writeFile(outputFile, t.generateAsm(), (err) => {
            if (err) {
                console.error("Error writing to file:", err);
                process.exit(1);
            }
            console.log(`ASM written to ${outputFile}`);
        });
    });
}

main();
