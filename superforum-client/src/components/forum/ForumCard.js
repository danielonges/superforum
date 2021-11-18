import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import Moment from 'react-moment';

import ForumContext from '../../context/forum/forumContext';

const ForumCard = ({ forum }) => {
    const forumContext = useContext(ForumContext);

    const { setForum } = forumContext;

    const onClick = (e) => {
        setForum(forum);
    };
    return (
        <Grid item xs={12} md={6}>
            <Card sx={{ display: 'flex', alignItems: 'center', padding: 1 }}>
                <CardActionArea
                    component={Link}
                    to={{
                        pathname: '/forum'
                    }}
                    onClick={onClick}
                    href='#'
                >
                    <CardContent sx={{ flex: 1 }}>
                        <Typography
                            component='h2'
                            variant='h6'
                            color='primary'
                            sx={{ mb: 2 }}
                        >
                            {forum.title}
                        </Typography>
                        <Container sx={{ ml: -2 }}>
                            <Typography variant='overline'>
                                {forum.forumCategory.category}
                            </Typography>
                            <Typography
                                variant='subtitle2'
                                color='text.secondary'
                                sx={{ mb: 3 }}
                            >
                                {`${forum.owner.username} • Created on `}
                                <Moment format='DD/MM/YYYY'>
                                    {forum.dateCreated.substring(
                                        0,
                                        forum.dateCreated.length - 5
                                    )}
                                </Moment>
                            </Typography>
                            <Typography variant='subtitle1' paragraph>
                                {`Description: ${forum.description}`}
                            </Typography>
                            <Typography variant='subtitle1' color='primary'>
                                Go to forum
                            </Typography>
                        </Container>
                    </CardContent>
                </CardActionArea>
            </Card>
        </Grid>
    );
};

ForumCard.propTypes = {
    forum: PropTypes.shape({
        date: PropTypes.string.isRequired,
        description: PropTypes.string.isRequired,
        image: PropTypes.string.isRequired,
        imageLabel: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired
    }).isRequired
};

export default ForumCard;
