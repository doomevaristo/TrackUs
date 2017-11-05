package com.marcosevaristo.tcc001.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.utils.ImageUtils;

public class BotaoFavorito extends ImageView {
    public static final int STYLE_BLACK = 0;
    public static final int STYLE_WHITE = 1;
    public static final int STYLE_STAR = 0;
    public static final int STYLE_HEART = 1;

    public static int posicao;

    private static final int DEFAULT_BUTTON_SIZE = 40;
    private static final boolean DEFAULT_FAVORITE = false;
    private static final boolean DEFAULT_ANIMATE_FAVORITE = true;
    private static final boolean DEFAULT_ANIMATE_UNFAVORITE = false;
    private static final int DEFAULT_ROTATION_DURATION = 400;
    private static final int DEFAULT_ROTATION_ANGLE = 360;
    private static final int DEFAULT_BOUNCE_DURATION = 300;
    private static final int FAVORITE_STAR_BLACK = R.mipmap.ic_star_black;
    private static final int FAVORITE_STAR_BORDER_BLACK = R.mipmap.ic_star_black_border;
    private static final int FAVORITE_HEART_BLACK = R.mipmap.ic_favorite_black;
    private static final int FAVORITE_HEART_BORDER_BLACK = R.mipmap.ic_favorite_black_border;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private int mButtonSize;
    private int mPadding;
    private boolean mFavorite;
    private boolean mAnimateFavorite;
    private boolean mAnimateUnfavorite;
    private int mFavoriteResource;
    private int mNotFavoriteResource;
    private int mRotationDuration;
    private int mRotationAngle;
    private int mBounceDuration;
    private int mColor;
    private int mType;

    private OnFavoriteChangeListener mOnFavoriteChangeListener;
    private OnFavoriteAnimationEndListener mOnFavoriteAnimationEndListener;
    private boolean mBroadcasting;

    public BotaoFavorito(Context context) {
        super(context);
        init(context, null);
    }

    public BotaoFavorito(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BotaoFavorito(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public interface OnFavoriteChangeListener {
        void onFavoriteChanged(BotaoFavorito buttonView, boolean favorite);
    }

    public void setOnFavoriteChangeListener(OnFavoriteChangeListener listener) {
        mOnFavoriteChangeListener = listener;
    }

    public interface OnFavoriteAnimationEndListener {
        void onAnimationEnd(BotaoFavorito buttonView, boolean favorite);
    }

    public void setOnFavoriteAnimationEndListener(OnFavoriteAnimationEndListener listener) {
        mOnFavoriteAnimationEndListener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        mButtonSize = ImageUtils.dpToPx(DEFAULT_BUTTON_SIZE, getResources());
        mFavorite = DEFAULT_FAVORITE;
        mAnimateFavorite = DEFAULT_ANIMATE_FAVORITE;
        mAnimateUnfavorite = DEFAULT_ANIMATE_UNFAVORITE;
        mFavoriteResource = FAVORITE_STAR_BLACK;
        mNotFavoriteResource = FAVORITE_STAR_BORDER_BLACK;
        mRotationDuration = DEFAULT_ROTATION_DURATION;
        mRotationAngle = DEFAULT_ROTATION_ANGLE;
        mBounceDuration = DEFAULT_BOUNCE_DURATION;
        mColor = STYLE_BLACK;
        mType = STYLE_STAR;
        if (attrs != null) {
            initAttributes(context, attrs);
        }
        setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                toggleFavorite();
            }
        });
        if (mFavorite) {
            setImageResource(mFavoriteResource);
        } else {
            setImageResource(mNotFavoriteResource);
        }
        setPadding(mPadding, mPadding, mPadding, mPadding);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.BotaoFavorito);
        if (attr != null) {
            try {
                mButtonSize = ImageUtils.dpToPx(attr.getInt(R.styleable.BotaoFavorito_fav_size, DEFAULT_BUTTON_SIZE), getResources());
                mAnimateFavorite = attr.getBoolean(R.styleable.BotaoFavorito_fav_animate_favorite, mAnimateFavorite);
                mAnimateUnfavorite = attr.getBoolean(R.styleable.BotaoFavorito_fav_animate_unfavorite, mAnimateUnfavorite);

                if (attr.getResourceId(R.styleable.BotaoFavorito_fav_favorite_image, 0) != 0
                        && attr.getResourceId(R.styleable.BotaoFavorito_fav_not_favorite_image, 0) != 0) {
                    mFavoriteResource = attr.getResourceId(R.styleable.BotaoFavorito_fav_favorite_image, FAVORITE_STAR_BLACK);
                    mNotFavoriteResource = attr.getResourceId(R.styleable.BotaoFavorito_fav_not_favorite_image, FAVORITE_STAR_BORDER_BLACK);
                } else {
                    setTheme(attr.getInt(R.styleable.BotaoFavorito_fav_type, STYLE_STAR));
                }

                mRotationDuration = attr.getInt(R.styleable.BotaoFavorito_fav_rotation_duration, mRotationDuration);
                mRotationAngle = attr.getInt(R.styleable.BotaoFavorito_fav_rotation_angle, mRotationAngle);
                mBounceDuration = attr.getInt(R.styleable.BotaoFavorito_fav_bounce_duration, mBounceDuration);
            } finally {
                attr.recycle();
            }
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private void setTheme(int type) {
        if (type == STYLE_STAR) {
            mFavoriteResource = FAVORITE_STAR_BLACK;
            mNotFavoriteResource = FAVORITE_STAR_BORDER_BLACK;
        } else {
            mFavoriteResource = FAVORITE_HEART_BLACK;
            mNotFavoriteResource = FAVORITE_HEART_BORDER_BLACK;
        }
    }

    private void setResources() {
        if (mFavorite) {
            setImageResource(mFavoriteResource);
        } else {
            setImageResource(mNotFavoriteResource);
        }
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mButtonSize, mButtonSize);
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        if (mFavorite != favorite) {
            mFavorite = favorite;
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnFavoriteChangeListener != null) {
                mOnFavoriteChangeListener.onFavoriteChanged(this, mFavorite);
            }
            updateFavoriteButton(favorite);
            mBroadcasting = false;
        }
    }

    public void setFavorite(boolean favorite, boolean animated) {
        if (favorite) {
            boolean orig = mAnimateFavorite;
            mAnimateFavorite = animated;
            setFavorite(favorite);
            mAnimateFavorite = orig;
        } else {
            boolean orig = mAnimateUnfavorite;
            mAnimateUnfavorite = animated;
            setFavorite(favorite);
            mAnimateUnfavorite = orig;
        }
    }

    public void toggleFavorite() {
        setFavorite(!mFavorite);
    }

    public void toggleFavorite(boolean animated) {
        if (!mFavorite) {
            boolean orig = mAnimateFavorite;
            mAnimateFavorite = animated;
            setFavorite(!mFavorite);
            mAnimateFavorite = orig;
        } else {
            boolean orig = mAnimateUnfavorite;
            mAnimateUnfavorite = animated;
            setFavorite(!mFavorite);
            mAnimateUnfavorite = orig;
        }
    }

    private void updateFavoriteButton(boolean favorite) {
        if (favorite) {
            if (mAnimateFavorite) {
                animateButton(favorite);
            } else {
                super.setImageResource(mFavoriteResource);
                if (mOnFavoriteAnimationEndListener != null) {
                    mOnFavoriteAnimationEndListener.onAnimationEnd(this, mFavorite);
                }
            }
        } else {
            if (mAnimateUnfavorite) {
                animateButton(favorite);
            } else {
                super.setImageResource(mNotFavoriteResource);
                if (mOnFavoriteAnimationEndListener != null) {
                    mOnFavoriteAnimationEndListener.onAnimationEnd(this, mFavorite);
                }
            }
        }
    }

    private void animateButton(boolean toFavorite) {
        final int startAngle = 0;
        int endAngle;
        float startBounce;
        float endBounce;
        if (toFavorite) {
            endAngle = mRotationAngle;
            startBounce = 0.2f;
            endBounce = 1.0f;
        } else {
            endAngle = -mRotationAngle;
            startBounce = 1.3f;
            endBounce = 1.0f;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(this, "rotation", startAngle, endAngle);
        rotationAnim.setDuration(mRotationDuration);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(this, "scaleX", startBounce, endBounce);
        bounceAnimX.setDuration(mBounceDuration);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(this, "scaleY", startBounce, endBounce);
        bounceAnimY.setDuration(mBounceDuration);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                if (mFavorite) {
                    setImageResource(mFavoriteResource);
                } else {
                    setImageResource(mNotFavoriteResource);
                }
            }
        });

        animatorSet.play(rotationAnim);
        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                if (mOnFavoriteAnimationEndListener != null) {
                    mOnFavoriteAnimationEndListener.onAnimationEnd(BotaoFavorito.this, mFavorite);
                }
            }
        });

        animatorSet.start();
    }

    public static final class Builder {
        private final Context context;

        private int mButtonSize = DEFAULT_BUTTON_SIZE;
        private boolean mFavorite = DEFAULT_FAVORITE;
        private boolean mAnimateFavorite = DEFAULT_ANIMATE_FAVORITE;
        private boolean mAnimateUnfavorite = DEFAULT_ANIMATE_UNFAVORITE;
        private int mFavoriteResource = FAVORITE_STAR_BLACK;
        private int mNotFavoriteResource = FAVORITE_STAR_BORDER_BLACK;
        private int mRotationDuration = DEFAULT_ROTATION_DURATION;
        private int mRotationAngle = DEFAULT_ROTATION_ANGLE;
        private int mBounceDuration = DEFAULT_BOUNCE_DURATION;
        private int mColor = STYLE_WHITE;
        private int mType = STYLE_BLACK;
        private boolean mCustomResources = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder size(int size) {
            this.mButtonSize = size;
            return this;
        }

        public Builder favorite(boolean favorite) {
            this.mFavorite = favorite;
            return this;
        }

        public Builder animateFavorite(boolean animation) {
            this.mAnimateFavorite = animation;
            return this;
        }

        public Builder animateUnfavorite(boolean animation) {
            this.mAnimateUnfavorite = animation;
            return this;
        }

        public Builder favoriteResource(int resource) {
            this.mFavoriteResource = resource;
            mCustomResources = true;
            return this;
        }

        public Builder notFavoriteResource(int recsource) {
            this.mNotFavoriteResource = recsource;
            mCustomResources = true;
            return this;
        }

        public Builder rotationDuration(int rotationDuration) {
            this.mRotationDuration = rotationDuration;
            return this;
        }

        public Builder rotationAngle(int rotationAngle) {
            this.mRotationAngle = rotationAngle;
            return this;
        }

        public Builder bounceDuration(int bounceDuration) {
            this.mBounceDuration = bounceDuration;
            return this;
        }

        public Builder color(int color) {
            this.mColor = color;
            mCustomResources = false;
            return this;
        }

        public Builder type(int type) {
            this.mType = type;
            mCustomResources = false;
            return this;
        }

        public BotaoFavorito create() {
            BotaoFavorito BotaoFavorito = new BotaoFavorito(context);
            BotaoFavorito.setSize(mButtonSize);
            BotaoFavorito.setFavorite(mFavorite, false);
            BotaoFavorito.setAnimateFavorite(mAnimateFavorite);
            BotaoFavorito.setAnimateUnfavorite(mAnimateUnfavorite);
            BotaoFavorito.setFavoriteResource(mFavoriteResource);
            BotaoFavorito.setNotFavoriteResource(mNotFavoriteResource);
            BotaoFavorito.setRotationDuration(mRotationDuration);
            BotaoFavorito.setRotationAngle(mRotationAngle);
            BotaoFavorito.setBounceDuration(mBounceDuration);
            if (!mCustomResources) {
                BotaoFavorito.setType(mType);
            }
            BotaoFavorito.setResources();

            return BotaoFavorito;
        }
    }

    public void setSize(int size) {
        this.mButtonSize = ImageUtils.dpToPx(size, getResources());
    }

    public void setPadding(int padding) {
        this.mPadding = ImageUtils.dpToPx(padding, getResources());
        setPadding(mPadding, mPadding, mPadding, mPadding);
    }

    public void setAnimateFavorite(boolean animation) {
        this.mAnimateFavorite = animation;
    }

    public void setAnimateUnfavorite(boolean animation) {
        this.mAnimateUnfavorite = animation;
    }

    public void setFavoriteResource(int favoriteResource) {
        this.mFavoriteResource = favoriteResource;
    }

    public void setNotFavoriteResource(int notFavoriteResource) {
        this.mNotFavoriteResource = notFavoriteResource;
    }

    public void setRotationDuration(int rotationDuration) {
        this.mRotationDuration = rotationDuration;
    }

    public void setRotationAngle(int rotationAngle) {
        this.mRotationAngle = rotationAngle;
    }

    public void setBounceDuration(int bounceDuration) {
        this.mBounceDuration = bounceDuration;
    }

    public void setType(int type) {
        this.mType = type;
        setTheme(type);
    }
}