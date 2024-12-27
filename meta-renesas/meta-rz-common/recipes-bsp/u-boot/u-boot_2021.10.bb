require u-boot-common_${PV}.inc
require u-boot.inc

DEPENDS += "bc-native dtc-native"

UBOOT_URL = "git://github.com/zhangyunduan-ly/renesas-u-boot-cip-ly.git"
BRANCH = "develop-v2l-ly"

SRC_URI = "${UBOOT_URL};branch=${BRANCH}"
SRCREV = "1d172365794789c27a8e603516eba45c8f7e7f02"
PV = "v2021.10+git${SRCPV}"
