#! /usr/bin/bash

echo "This script will delete .gradle in your home directory! To avoid error, close android studio first."
read -p "Delete ~/.gradle? [Y/n] " confirm && [[ $confirm == [yY] || $confirm == [yY][eE][sS] ]] || exit 1
rm -rfv ~/.gradle
