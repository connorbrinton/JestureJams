package com.c2.sound;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;

import de.sciss.jcollider.Constants;
import de.sciss.jcollider.Control;
import de.sciss.jcollider.GraphElem;
import de.sciss.jcollider.Group;
import de.sciss.jcollider.NodeWatcher;
import de.sciss.jcollider.Server;
import de.sciss.jcollider.Server.CompletionAction;
import de.sciss.jcollider.Synth;
import de.sciss.jcollider.SynthDef;
import de.sciss.jcollider.UGen;
import de.sciss.jcollider.UGenChannel;
import de.sciss.net.OSCBundle;

public class SoundGenerator implements LeapParameterListener {

	Server server;
	boolean receivingParameters = false;
	public NodeWatcher nw = null;
	public Group grpAll;

	public void start() {
		try {
			server = new Server("SoundGenerator");
			killOnShutdown();
			server.start();
			server.boot();
			server.addDoWhenBooted(new CompletionAction() {

				@Override
				public void completion(Server server) {
					try {
						initServer();
						sendDefs();
						onLeapParametersChanged(null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void killOnShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				try {
					server.quit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * 
	 */

	public void initServer() throws IOException {
		// sendDefs();
		if (!server.didWeBootTheServer()) {
			server.initTree();
			server.notify(true);
		}
		// if( nw != null ) nw.dispose();
		nw = NodeWatcher.newFrom(server);
		grpAll = Group.basicNew(server);
		nw.register(server.getDefaultGroup());
		nw.register(grpAll);
		server.sendMsg(grpAll.newMsg());
	}

	public void sendDefs() {
		File dir = new File("res/synthdefs/");
		for (File syn : dir.listFiles()) {
			try {
				//System.out.println("trying to load "+syn+"...");
				for (SynthDef sd : SynthDef.readDefFile(syn)) {
					System.out.println("New Synth: " + sd.getName());
					sd.send(server);
				}
			} catch (IOException e) {
				System.out
						.println(syn
								+ " is an invalid synthdef! trying to continue without it! if you notice funny behavior, chances are its because of this.");
				e.printStackTrace();
			}
		}
	}

/*	public void onFirstLeapParameters(LeapParameters newParameters) {
		System.out.println("Setting up Synths.");
		// We want to enable synths here.
		float pitch = 300;
		int out = 18;
		float amp = (float) 0.4;
		float fadetime = 9;
		float pan = (float) 0.0;

		Group instGroup = Group.basicNew(server);
		try {
			nw.register(instGroup);
			server.sendMsg(instGroup.newMsg());
			Synth mixerchannel = new Synth("StereoMixerChannel", new String[] {
					"inbus", "outbus", "amp", "pan", "dur", "gate" },
					new float[] { out, 0, amp, fadetime, pan, 1 }, instGroup,
					Constants.kAddToHead);
			Synth synth = new Synth("stereosaw",
					new String[] { "out", "freq" }, new float[] { out, pitch },
					mixerchannel, Constants.kAddAfter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done!");
	}*/
	
	public void onFirstLeapParameters(LeapParameters newParameters) {
        GraphElem   f       = null;
        GraphElem   g, h;
        Control     c       = Control.kr( new String[] { "resinv" }, new float[] { 0.5f });
        UGenChannel reso    = c.getChannel( 0 );
        Synth       synth;
        Random      r       = new Random( System.currentTimeMillis() );
        String      defName = "JNoiseBusiness1b";
        OSCBundle   bndl;
        SynthDef    def;
        long        time;
        
        f = null;
        for( int i = 0; i < 4; i++ ) {
            g = UGen.ar( "*", UGen.ar( "LFSaw", UGen.kr( "midicps", UGen.kr( "MulAdd",
                UGen.kr( "LFPulse", UGen.ir( 0.06f ), UGen.ir( 0 ), UGen.ir( 0.5f )),
                    UGen.ir( 2 ), UGen.array( UGen.ir( 34 + r.nextFloat() * 0.2f ),
                                              UGen.ir( 34 + r.nextFloat() * 0.2f ))))),
                  UGen.ir( 0.01f ));
            f = (f == null) ? g : UGen.ar( "+", f, g );
        }
        h   = UGen.kr( "LinExp", UGen.kr( "SinOsc", UGen.ir( 0.07f )),
                  UGen.ir( -1 ), UGen.ir( 1 ), UGen.ir( 300 ), UGen.ir( 5000 ));
        f   = UGen.ar( "softclip", UGen.ar( "RLPF", f, h, reso ));
        f   = UGen.ar( "softclip", UGen.ar( "RLPF", f, h, reso ));
        def = new SynthDef( defName, UGen.ar( "Out", UGen.ir( 0 ), f ));
        
        synth = Synth.basicNew( defName, server );
        try {
            def.send( server, synth.newMsg( server.asTarget(),
                new String[] { "resinv" }, new float[] { 0.98f }));
            time = System.currentTimeMillis();
            for( int i = 500; i < 5000; i += 250 ) {
                bndl = new OSCBundle( time + i );
                bndl.addPacket( synth.setMsg( "resinv", r.nextFloat() * 0.8f + 0.015f ));
                server.sendBundle( bndl );
            }
            bndl = new OSCBundle( time + 5500 );
            bndl.addPacket( synth.freeMsg() );
            server.sendBundle( bndl );
        }
        catch( IOException e1 ) {
            System.err.println( e1 );
        }
	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		if (!receivingParameters) {
			receivingParameters = true;
			onFirstLeapParameters(newParameters);
		} else {
			// Do OnChanged stuff
		}
	}

}
