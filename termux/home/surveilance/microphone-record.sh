#!/data/data/com.termux/files/usr/bin/bash
WORKSPACEDIR="$HOME/izyware/izy-idman-tools/id/me/microphone"
mkdir -p "$WORKSPACEDIR"
mkdir -p "$WORKSPACEDIR/inprogress"
mkdir -p "$WORKSPACEDIR/complete"

monitoring=1        # 1=info 2=debug
poll_interval=1     # seconds between polls
chunk_duration=600  # in seconds 

while true
do
    while true
    do
        # Get JSON status
        info=$(termux-microphone-record -i 2>/dev/null)
        (( $monitoring >= 2)) && echo $info
        if ! echo "$info" | grep -q '"isRecording": true'; then
            (( $monitoring >= 1)) && echo "Done recording. Move to completed state"
            mv $WORKSPACEDIR/inprogress/*.* $WORKSPACEDIR/complete/
            break
        fi
        sleep "$poll_interval"
    done
    timestamp=$(date +"%Y-%m-%d_%H-%M-%S")
    outfile="$WORKSPACEDIR/inprogress/rec_$timestamp.$chunk_duration.m4a"
    (( $monitoring >= 1)) && echo "Starting new chunk: $outfile"
    termux-microphone-record -d -l $chunk_duration -f "$outfile" 
done
