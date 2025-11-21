import os
import subprocess
import argparse
import shutil

SUPPORTED_EXTENSIONS = (".wav", ".mp4", ".m4a", ".mp3", ".ogg", ".flac")

def get_rms_ffmpeg(path):
    """Return RMS (approx) in dBFS using ffmpeg volumedetect, or None on error."""
    cmd = [
        "ffmpeg",
        "-i", path,
        "-af", "volumedetect",
        "-f", "null",
        "-"  # output discarded
    ]
    try:
        result = subprocess.run(cmd, stderr=subprocess.PIPE, stdout=subprocess.PIPE, text=True)
        stderr = result.stderr
        for line in stderr.splitlines():
            line = line.strip()
            if "mean_volume:" in line:
              parts = line.split()
              try:
                  idx = parts.index("mean_volume:")
                  db_value = float(parts[idx + 1])  # value comes after "mean_volume:"
                  return db_value
              except (ValueError, IndexError):
                  continue
        return None
    except Exception as e:
        print(f"Error processing {path}: {e}")
        return None

def scan_folder(folder, threshold_db, recursive):
    audio_files = []
    silent_files = []
    skipped_files = []

    if recursive:
        file_iter = (
            os.path.join(root, f)
            for root, _, files in os.walk(folder)
            for f in files
        )
    else:
        file_iter = (os.path.join(folder, f) for f in os.listdir(folder))

    for path in file_iter:
        if not path.lower().endswith(SUPPORTED_EXTENSIONS):
            continue

        rms_db = get_rms_ffmpeg(path)
        if rms_db is None:
            skipped_files.append(path)
            continue

        if rms_db > threshold_db:
            audio_files.append((path, rms_db))
        else:
            silent_files.append((path, rms_db))

    return audio_files, silent_files, skipped_files

def main():
    parser = argparse.ArgumentParser(description="Scan and group audio files into silent and audible")
    parser.add_argument("folder", help="Folder containing audio files")
    parser.add_argument("--threshold", "-t", type=float, default=-50.0,
                        help="Mean volume threshold in dB to determine audio presence (e.g., -50)")
    parser.add_argument("--move", "-m", type=bool, default=False,
                        help="Move into group folders")

    args = parser.parse_args()

    audio, silent, skipped = scan_folder(args.folder, args.threshold, False)
    folder_audible = os.path.join(args.folder, "../audible")
    os.makedirs(folder_audible, exist_ok=True)

    folder_silent = os.path.join(args.folder, "../silent")
    os.makedirs(folder_silent, exist_ok=True)

    print("\nAudible files:")
    for f, rms_db in audio:
        if args.move:
          print(f"  ✔ {f} (mean_volume={rms_db:.2f} dB) -> {folder_audible}")
          shutil.move(f, folder_audible)
        else:
          print(f"  ✔ {f} (mean_volume={rms_db:.2f} dB)")

    print("\nSilent files:")
    for f, rms_db in silent:
        if args.move:
          print(f"  ✖ {f} (mean_volume={rms_db:.2f} dB) -> {folder_silent}")
          shutil.move(f, folder_silent)
        else:
          print(f"  ✖ {f} (mean_volume={rms_db:.2f} dB)")
        

    print("\nSkipped/unreadable files:")
    for f in skipped:
        print(f"  ⚠ {f}")

if __name__ == "__main__":
    main()
