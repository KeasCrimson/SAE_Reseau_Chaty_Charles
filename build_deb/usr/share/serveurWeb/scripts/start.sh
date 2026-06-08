#!/bin/bash

cd /usr/share/serveurWeb/bin

# nohup permet de faire tourner le programme meme quand le terminal est fermé
nohup java ServeurMain > /dev/null 2>&1 &

echo $! > /usr/share/serveurWeb/run/myweb.pid

echo "Serveur Web start"