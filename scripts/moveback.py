import os
import sys

# Use inside of server patch dir
# py ../../scripts/moveback.py 'Rewrite chunk system'
patch_target = 992 # TODO: Update this


def increment_number(filename):
    current_number = int(filename[:4])
    new_number = current_number + 1
    return f"{new_number:04d}-{filename[5:]}"


if len(sys.argv) != 2:
    print("python moveback.py '<commit title>'")
    sys.exit(1)

input_string = sys.argv[1].replace(' ', '-').lower()
if len(input_string) < 5:
    print("Commit title is too short")
    sys.exit(1)

matching_files = [file for file in os.listdir() if input_string in file.lower()]

if len(matching_files) == 0:
    print("No file found matching the given string")
    sys.exit(1)

matching_file = matching_files[0]
print(f"Found: {matching_file}")

# Move all files after the target one up
for file in os.listdir():
    num = int(file[:4])
    if num >= patch_target:
        new_filename = increment_number(file)
        os.rename(file, new_filename)
        print(f"Renamed {file} to {new_filename}")

# Rename the file to the target
new_filename = f"{patch_target:04d}-{matching_file[5:]}"
os.rename(matching_file, new_filename)
print(f"Renamed {matching_file} to {new_filename}")
