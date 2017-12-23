# FileSecure 3
Command Line utility to make files inaccessible or unreadable to other programs.

# Prequisite:

Java 8 Runtime

# Installation:

1. Download & extract FileSecure.zip from builds folder;

2. cd to FileSecure

> Windows:

3. Execute install.bat

4. Add "C:\FileSecure\" to PATH variable 

> Linux:

3. $ sh install.sh (provide the sudo password)

4. Add "export PATH=$PATH:/opt/FileSecure" to /etc/profile

# Usage

1. cd to dir in which files needed to be secured and run

    $ secure

2. use the option number to select the option


> Select Secure File

All the files in the current directory and sub directories will be listed

You can either select any one (option number) or all (0) or go back(-1)

Enter the Secret Key

The selected files will be encrypted and moved to secure folder


> Retrive Secured File

All the secured files in the current directory will be listed

You can either select any one (option number) or all (0) or go back(-1)

Enter the Secret Key

All the selected files will be decrypted


> Retrive & Remove Secured File

All the secured files in the current directory will be listed

You can either select any one (option number) or all (0) or go back(-1)

Enter the Secret Key

All the selected files will be decrypted and secured file will be deleted


> Exit

To exit the utility


# Contribute

For now I've simply reverted the bytes of the file, but you can add some real Encryption/Decryption algorithms.


I will work on adding some other algorithms and creating a GUI for this utility
