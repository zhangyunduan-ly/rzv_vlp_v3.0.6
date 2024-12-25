require u-boot-common_${PV}.inc
require u-boot.inc

DEPENDS += "bc-native dtc-native"

UBOOT_URL = "git://github.com/zhangyunduan-ly/renesas-u-boot-cip-ly.git"
BRANCH = "develop-v2l-ly"

SRC_URI = "${UBOOT_URL};branch=${BRANCH}"
SRCREV = "94dcf230a69046f281fb89e69614c0d54815ff81"
PV = "v2021.10+git${SRCPV}"
