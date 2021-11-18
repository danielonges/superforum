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

import UserContext from '../../context/user/userContext';
import { Button } from '@mui/material';

const UserCard = ({ user: currUser }) => {
    const userContext = useContext(UserContext);

    const { user, blockUser } = userContext;

    const onClickBlock = (e, block) => {
        blockUser(currUser, block, user.id);
    };

    return (
        <Grid item xs={12} md={6}>
            <Card sx={{ display: 'flex', alignItems: 'center', padding: 2 }}>
                <CardContent sx={{ flex: 1 }}>
                    <Typography
                        component='h2'
                        variant='h6'
                        color='primary'
                        sx={{ mb: 2 }}
                    >
                        {currUser.username}
                    </Typography>
                    {currUser.isBlocked && (
                        <Typography
                            variant='overline'
                            fontStyle='italic'
                            paragraph
                        >
                            Blocked
                        </Typography>
                    )}
                    <Container sx={{ ml: -2 }}>
                        <Typography variant='subtitle1' sx={{ mb: 1 }}>
                            Email: {currUser.email}
                        </Typography>
                        <Typography variant='subtitle1' paragraph>
                            Bio: {currUser.bio}
                        </Typography>
                        {currUser.userType === 'ADMIN' ? (
                            <Typography variant='subtitle1' color='primary'>
                                Admin User
                            </Typography>
                        ) : (
                            <Typography variant='subtitle1'>
                                Normal User
                            </Typography>
                        )}
                    </Container>
                </CardContent>
                <CardContent sx={{ flex: 0.2 }}>
                    {currUser.id !== user.id &&
                        (currUser.isBlocked ? (
                            <Button
                                variant='outlined'
                                color='primary'
                                onClick={(e) => onClickBlock(e, false)}
                            >
                                Unblock User
                            </Button>
                        ) : (
                            <Button
                                variant='outlined'
                                color='error'
                                onClick={(e) => onClickBlock(e, true)}
                            >
                                Block User
                            </Button>
                        ))}
                </CardContent>
            </Card>
        </Grid>
    );
};

UserCard.propTypes = {
    forum: PropTypes.shape({
        date: PropTypes.string.isRequired,
        description: PropTypes.string.isRequired,
        image: PropTypes.string.isRequired,
        imageLabel: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired
    }).isRequired
};

export default UserCard;
