package fr.rhumun.game.worldcraftopengl.outputs.audio;

import fr.rhumun.game.worldcraftopengl.Game;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class AudioManager {

    private long device;
    private long context;
    private final Game game;

    public AudioManager(Game game) {
        this.game = game;
    }

    public void init() {
        String deviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        device = ALC10.alcOpenDevice(deviceName);
        if (device == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to open OpenAL device.");
        } else {
            game.log("OpenAL device opened successfully.");
        }

        context = ALC10.alcCreateContext(device, new int[]{0});
        if (context == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        } else {
            game.log("OpenAL context created successfully.");
        }

        ALC10.alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        if (alcCapabilities == null) {
            throw new IllegalStateException("Failed to get ALC capabilities.");
        }

        AL.createCapabilities(alcCapabilities);
        game.log("OpenAL capabilities created successfully.");
    }


    public void cleanup() {
        // Fermer OpenAL
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }

    public int loadSound(String path) {
        int buffer = alGenBuffers();
        game.debug("Loading sound: " + path);

        try (MemoryStack stack = stackPush()) {
            STBVorbisInfo info = STBVorbisInfo.mallocStack(stack);
            ShortBuffer pcm = loadOggVorbis(path, info);

            alBufferData(buffer, AL_FORMAT_MONO16, pcm, info.sample_rate());
        } catch (Exception e) {
            game.errorLog(e);
        }

        return buffer;
    }

    private ShortBuffer loadOggVorbis(String path, STBVorbisInfo info) throws Exception {
        if (!Files.exists(Paths.get(path))) {
            throw new Exception("Fichier audio non trouvé: " + path);
        }

        ShortBuffer pcm;
        long decoder = NULL;
        try (MemoryStack stack = stackPush()) {
            IntBuffer error = stack.mallocInt(1); // Pour recueillir l'état d'erreur
            decoder = STBVorbis.stb_vorbis_open_filename(path, error, null);
            if (decoder == NULL) {
                throw new Exception("Erreur lors du chargement du fichier OGG: " + path);
            }

            // Obtenir des informations audio
            STBVorbis.stb_vorbis_get_info(decoder, info);

            // Calculer le nombre total d'échantillons
            int numSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder) * info.channels();

            // Remplacer stackMallocShort par MemoryUtil.memAllocShort
            pcm = MemoryUtil.memAllocShort(numSamples);

            // Charger les échantillons audio dans le tampon PCM
            STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, info.channels(), pcm);
        } catch (Exception e) {
            throw new Exception("Erreur pendant le traitement audio: " + e.getMessage(), e);
        } finally {
            // S'assurer que le décodeur est fermé
            if (decoder != NULL) {
                STBVorbis.stb_vorbis_close(decoder);
            }
        }

        return pcm;
    }

    public int createSource(int buffer) {
        // Créer une source OpenAL
        int source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
        return source;
    }

    public void playSound(final Sound sound) {
        if(sound == null) return;
        alSourcef(sound.getId(), AL_GAIN, 1);
        alSourcePlay(sound.getId());
    }

    public void playSound(final Sound sound, final float volume) {
        if(sound == null) return;
        alSourcef(sound.getId(), AL_GAIN, volume);
        alSourcePlay(sound.getId());
    }
}
