prime-patcher
=============

This project can be used to diff, patch, and randomize Metroid Prime and Metroid Prime 2: Echoes. It's not quite done yet.

Working Features:
* Iso + PAK extraction
* Iso -> Iso direct patching
* Iso <-> Iso diff (patch generation)

The latest release (updated more infrequently than the latest build below) can be found under the github releases tab

![build status](https://circleci.com/gh/Pwootage/prime-patcher.png?circle-token=0cc2559ae2175225d34e6aeaf08ca37446bb7dec)
| [Latest Successful Build](https://circleci.com/api/v1/project/Pwootage/prime-patcher/latest/artifacts/0//home/ubuntu/prime-patcher/build/libs/patcher-0.1.1-SNAPSHOT.jar?branch=master&filter=successful)
| [CircleCI](https://circleci.com/gh/Pwootage/prime-patcher)

# Some common commands

All commands should be prefixed with `java -jar patcher-0.1.1-SHAPSHOT.jar `

Extract an iso (but not embedded PAKs)
* `extract -fq -o output-directory/ -i input/GM8E01.iso`

Extract an iso (and embedded PAKs)
* `extract -fpq -o output-directory/ -i input/GM8E01.iso`

Repack an iso (auto-detects PAKs if they are extracted by this tool)
* `repack -fq -i input/directory/ -o output-file.iso`

# How do I randomize?

It's not completely working, but you're going to want a [config.json](config.json) and then run patcher a couple times
from a command prompt/terminal. Hopefully the error message is useful if it doesn't work! No guarantees.

Requires JRE8 or JDK8

## Generate Seed
`java -jar patcher-0.1.1-SHAPSHOT.jar randomize -i GM8E01.iso -c config.json`

## Apply patches
`java -jar patcher-0.1.1-SNAPSHOT.jar patch -fq -i mprime.iso -o out/mprime-randomized.iso prime-patchfiles-master/widescreen/patchfile.json prime-patchfiles-master/frigateskip-GM8E01-0-00.json rando/randomize.json`

# Credits

Parax
* Reverse Engineering
* PWE (for checking my work)
* Template files for script objects

Miles
* Reverse Engineering

Nintendo/Retro Studios
* Releasing Metroid Prime(s)

Everyone else
* Being awesome

# LICENSE
    Copyright (C) 2016 Christopher Freestone

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

