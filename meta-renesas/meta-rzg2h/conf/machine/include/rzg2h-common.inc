require conf/machine/include/rzg2-common.inc
require include/ecc-control.inc

MACHINEOVERRIDES =. "rzg2h:"

MACHINE_FEATURES_append = " opengles hwh264dec hwh264enc hwh265dec bayer2raw"
