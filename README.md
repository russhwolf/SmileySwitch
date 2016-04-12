# SmileySwitch

This implementation makes a few changes from the original animation, in order to highlight various properties of switches
in Android. There are three different pieces of state we're interested in. The eyes animate as we move in and out of the
`pressed` state. The mouth and the track color animate based on the `checked` state. Finally the head deforms and turns
according to the position of the switch along the track.