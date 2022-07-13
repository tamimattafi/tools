#!/bin/bash

function find_real_path() {
  local main_dir=$PWD;
  local file="$0";
  cd "$(dirname "$file")";
  local link=$(readlink "$(basename "$file")");
  while [ "$link" ]; do
    cd "$(dirname "$link")"
    link=$(readlink "$(basename "$file")")
  done;
  local real_path="$PWD/$(basename "$file")";
  cd "$main_dir";
  echo "$real_path";
}

function unzip_to_folder() { 
  local file="$0";
  local real_path=$(find_real_path);
  local folder=${real_path%.*};
  unzip "$real_path" -d "$folder";
}

function unzip_and_remove() {
  local file="$0";
  local real_path=$(find_real_path);
  unzip_to_folder "$real_path";
  rm -rf "$real_path";
}

function unjar_and_remove() {
  ## Replace with the path to your procyon.jar
  ## Download from https://github.com/mstrobel/procyon/releases
  local procyon="/Users/tamimattafi/jars/procyon-decompiler-0.6.0.jar"

  local file="$0";
  local real_path=$(find_real_path);
  local folder=${real_path%.*};
  local src_folder="$folder/src/main/java";
  java -jar "$procyon" -jar "$real_path" -o "$src_folder";
  rm -rf "$real_path";
}

function unclass_and_remove() {
  ## Replace with the path to your procyon.jar
  ## Download from https://github.com/mstrobel/procyon/releases
  local procyon="/Users/tamimattafi/jars/procyon-decompiler-0.6.0.jar"

  local file="$0";
  local real_path=$(find_real_path);
  local folder=${real_path%.*};
  java -jar "$procyon" "$real_path" -o "$folder";
  rm -rf "$real_path";
}

export -f unzip_to_folder
export -f unzip_and_remove
export -f unjar_and_remove
export -f unclass_and_remove
export -f find_real_path

find . -name '*.ear' -type f -exec /bin/bash -c 'unzip_to_folder "$0"' {}  \;
find . -name '*.war' -type f -exec /bin/bash -c 'unzip_and_remove "$0"' {}  \;
find . -name '*.jar' -type f -exec /bin/bash -c 'unjar_and_remove "$0"' {}  \;
find . -name '*.class' -type f -exec /bin/bash -c 'unclass_and_remove "$0"' {}  \;
