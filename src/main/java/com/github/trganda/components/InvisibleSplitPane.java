package com.github.trganda.components;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

/**
 * A split pane that does not paint anything
 */
public class InvisibleSplitPane extends JSplitPane
{
    private static final long serialVersionUID = 429222851935165219L;

    /**
     * {@inheritDoc}
     */
    public InvisibleSplitPane ()
    {
        super ();
    }

    /**
     * {@inheritDoc}
     */
    public InvisibleSplitPane ( int newOrientation, boolean newContinuousLayout, Component newLeftComponent,
                                Component newRightComponent )
    {
        super ( newOrientation, newContinuousLayout, newLeftComponent, newRightComponent );
    }

    /**
     * {@inheritDoc}
     */
    public InvisibleSplitPane ( int newOrientation, boolean newContinuousLayout )
    {
        super ( newOrientation, newContinuousLayout );
    }

    /**
     * {@inheritDoc}
     */
    public InvisibleSplitPane ( int newOrientation, Component newLeftComponent, Component newRightComponent )
    {
        super ( newOrientation, newLeftComponent, newRightComponent );
    }

    /**
     * {@inheritDoc}
     */
    public InvisibleSplitPane ( int newOrientation )
    {
        super ( newOrientation );
    }

    /**
     * Notification from the <code>UIManager</code> that the L&F has changed.
     * Replaces the current UI object with the latest version from the
     * <code>UIManager</code>.
     *
     * @see JComponent#updateUI
     */
    public void updateUI ()
    {
        SplitPaneUI ui = new InvisibleSplitPaneUI();
        setUI ( ui );
        revalidate ();
    }

    /**
     * The look and feel UI that draws nothing
     */
    private class InvisibleSplitPaneUI extends BasicSplitPaneUI
    {
        /**
         * Create the UI
         */
        public InvisibleSplitPaneUI ()
        {

        }

        /**
         * Installs the UI defaults.
         */
        protected void installDefaults ()
        {
            super.installDefaults ();

            splitPane.setBorder ( null );
        }

        /**
         * Creates the default divider.
         */
        public BasicSplitPaneDivider createDefaultDivider ()
        {
            BasicSplitPaneDivider d = new BasicSplitPaneDivider(this)
            {
                private static final long serialVersionUID = 225334791139486944L;

                public void paint ( Graphics g )
                {

                }
            };
            d.setBorder ( null );
            return d;
        }
    }
}
