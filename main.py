import os
import re

# Set the directory where the files are located
dir_path = '/Users/jason/IdeaProjects/PaperMC/Paper/patches/unapplied/server'

# Change to the directory
os.chdir(dir_path)

# Regex pattern to match the file names
pattern = r'^(\d+)-(.*\.patch)$'

# List of files in the directory
files = os.listdir(dir_path)

# Sort the files numerically
files.sort(key=lambda x: int(re.match(pattern, x).group(1)))

for file in files:
    # Match the file name against the pattern
    match = re.match(pattern, file)
    if match:
        # Extract the current number and description
        current_number = int(match.group(1))
        description = match.group(2)

        # Calculate the new number
        new_number = current_number + 4

        # Construct the new file name
        new_file_name = f'{str(new_number).zfill(4)}-{description}'

        # Rename the file
        os.rename(file, new_file_name)

print('Files renamed successfully.')
