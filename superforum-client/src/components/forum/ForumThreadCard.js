import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Moment from 'react-moment';
import { ContentPaste } from '@mui/icons-material';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';

const ForumThreadCard = (props) => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { forumThread } = props;
    const { setForumThread, incrementViewCount } = forumContext;
    const { user } = userContext;

    const onClick = () => {
        incrementViewCount(forumThread);
        setForumThread(forumThread);
    };
    return (
        <Grid item xs={12} md={6}>
            <CardActionArea
                component={Link}
                to={'/forumthread'}
                onClick={onClick}
                href='#'
            >
                <Card sx={{ display: 'flex', alignItems: 'center' }}>
                    <CardContent sx={{ flex: 1 }}>
                        <Typography component='h2' variant='h6'>
                            {forumThread.title}
                        </Typography>
                        <Typography variant='subtitle2' color='text.secondary'>
                            {`${forumThread.owner.username} • Created on `}
                            <Moment format='DD/MM/YYYY'>
                                {forumThread.dateCreated.substring(
                                    0,
                                    forumThread.dateCreated.length - 5
                                )}
                            </Moment>
                        </Typography>
                        <Typography variant='subtitle1' paragraph>
                            {forumThread.description}
                        </Typography>
                        <Typography variant='subtitle1' color='primary'>
                            Go to thread
                        </Typography>
                    </CardContent>
                    <CardContent sx={{ flex: 0.5 }}>
                        <Typography variant='subtitle2' paragraph>
                            {`Posts: ${forumThread.posts.length}`}
                        </Typography>
                        <Typography variant='subtitle2' paragraph>
                            {`Views: ${forumThread.views}`}
                        </Typography>
                    </CardContent>
                </Card>
            </CardActionArea>
        </Grid>
    );
};

export default ForumThreadCard;
