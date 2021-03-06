/*
The following is the license of LiteOS.

This file is part of LiteOS.
Copyright Qing Cao, 2007-2008, University of Illinois , qcao2@uiuc.edu

LiteOS is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

LiteOS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LiteOS.  If not, see <http://www.gnu.org/licenses/>.
 */

package tools.terminal;

import java.util.ArrayList;

/**
 * The class for the rm command.
 */
public class rmCommand implements cmdcontrol {

	private byte[] reply = new byte[64];

	// Return the total number of commands will be used
	public int setNewCommand(String[] options, int optioncount,
			String[] parameters, int parametercount, fileDirectory fdir) {

		fileNode currentnode = fdir.getCurrentNode();
		int currentAddress = currentnode.getNodeAddress();
		int currentBlock = currentnode.getBlock();

		byte[] filename = parameters[0].getBytes();

		reply[0] = (new Integer(filename.length + 4)).byteValue();
		reply[1] = (byte) 161;
		reply[2] = (new Integer(currentAddress)).byteValue();
		reply[3] = (new Integer(currentBlock)).byteValue();

		System.arraycopy(filename, 0, reply, 4, filename.length);

		return 1; // To change body of implemented methods use File | Settings |
					// File Templates.
	}

	// Return the delay in milliseconds
	public int getDelay() {
		return 2000; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	// return the command will be used
	public byte[] getNewCommand(int index) {
		return reply;
	}

	public void handleresponse(String[] options, int optioncount,
			String[] parameters, int parametercount, ArrayList reply,
			fileDirectory fdir) {

		String filenodeName = parameters[0];
		fileNode cnode = fdir.getCurrentNode();

		int start = 5;

		while (reply.size() > 0) {
			byte[] response = (byte[]) reply.remove(0);
			int msgLength = (0x000000FF & ((int) response[start]));
			int blockaddress = (0x000000FF & ((int) response[start + 3]));

			if (blockaddress == 0) {
				colorOutput.println(colorOutput.COLOR_BRIGHT_RED,
						"No such file or directory. Rm fails.");
				return;
			} else
				cnode.deleteChildByName(filenodeName);

		}

		return;
	}
}
