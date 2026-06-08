#!/bin/bash

PID_FILE="/usr/share/serveurWeb/run/myweb.pid"

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    
    kill $PID
    
    rm "$PID_FILE"
    
    echo "Serveur Web Stop"
else
    echo "Erreur : fichier $PID_FILE introuvable"
fi