package org.jzy3d.demos.groovy.fred

int ratio = 150
def st = new SpaceTime(ratio)
assert st.spaces.last().events.size() == 4
Calculator calculator = new Calculator(st)
for (int i=0;i<(ratio*2*0.57);i++) {
    calculator.calc()
    assert st.spaces.last().events.size() == 0
}
calculator.calc()
assert st.spaces.last().events.size() == 4
