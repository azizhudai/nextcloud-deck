package it.niedermann.nextcloud.deck.ui.about;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.niedermann.nextcloud.deck.BuildConfig;
import it.niedermann.nextcloud.deck.R;
import it.niedermann.nextcloud.deck.util.LinkUtil;

public class AboutFragmentCreditsTab extends Fragment {

    @BindView(R.id.about_version)
    TextView aboutVersion;
    @BindView(R.id.about_maintainer)
    TextView aboutMaintainer;
    @BindView(R.id.about_translators)
    TextView aboutTranslators;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_credits_tab, container, false);
        ButterKnife.bind(this, v);
        LinkUtil.setHtml(aboutVersion, getString(R.string.about_version, getVersionStrongTag(getResources())));
        LinkUtil.setHtml(aboutMaintainer, LinkUtil.concatenateResources(v.getResources(),
                R.string.anchor_start, R.string.url_maintainer, R.string.anchor_middle, R.string.about_maintainer, R.string.anchor_end));
        LinkUtil.setHtml(aboutTranslators,
                v.getResources().getString(R.string.about_translators_transifex, LinkUtil.concatenateResources(v.getResources(),
                        R.string.anchor_start, R.string.url_translations, R.string.anchor_middle, R.string.about_translators_transifex_label, R.string.anchor_end
                )));
        return v;
    }

    private static String getVersionStrongTag(Resources resources) {
        return new StringBuilder()
                .append(resources.getString(R.string.strong_start))
                .append("v")
                .append(BuildConfig.VERSION_NAME)
                .append(resources.getString(R.string.strong_end)).toString();
    }
}