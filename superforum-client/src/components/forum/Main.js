import * as React from 'react';
import PropTypes from 'prop-types';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';

import ForumCard from './ForumCard';
import ForumThreadCard from './ForumThreadCard';

const Main = (props) => {
    const { forums, threads, title } = props;

    return (
        <Grid item xs={12}>
            <Typography variant='h6' gutterBottom>
                {title}
            </Typography>
            <Divider sx={{ mb: 3 }} />
            <Grid container spacing={4} columns={1}>
                {forums != null &&
                    (forums.length > 0 ? (
                        forums.map((forum) => (
                            <ForumCard key={forum.id} forum={forum} />
                        ))
                    ) : (
                        <Grid item xs={12}>
                            <Typography
                                variant='subtitle1'
                                color='text.secondary'
                                gutterBottom
                                sx={{ margin: 1 }}
                            >
                                No forums!
                            </Typography>
                        </Grid>
                    ))}
                {threads != null &&
                    (threads.length > 0 ? (
                        threads.map((forumThread, i) => (
                            <ForumThreadCard
                                key={i}
                                forumThread={forumThread}
                            />
                        ))
                    ) : (
                        <Grid item xs={12}>
                            <Typography
                                variant='subtitle1'
                                color='text.secondary'
                                gutterBottom
                                sx={{ margin: 1 }}
                            >
                                No threads in this forum!
                            </Typography>
                        </Grid>
                    ))}
            </Grid>
        </Grid>
    );
};

Main.propTypes = {
    forums: PropTypes.arrayOf(PropTypes.object),
    threads: PropTypes.arrayOf(PropTypes.object),
    title: PropTypes.string.isRequired
};

export default Main;
