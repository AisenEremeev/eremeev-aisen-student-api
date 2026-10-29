#!/usr/bin/env bash
set -euo pipefail
root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
mkdir -p "$root/logs"
{
    echo 'Student: Aisen Eremeev'
    printf 'Date: '; date --iso-8601=seconds
    printf 'Username: '; whoami
    echo 'Operating system:'; cat /etc/os-release
    echo 'Kernel:'; uname -r
    echo 'CPU:'; lscpu
    echo 'Memory:'; free -h
    echo 'Disk usage and available disk space:'; df -h
} | tee "$root/logs/Eremeev_Aisen_system_report.log"
