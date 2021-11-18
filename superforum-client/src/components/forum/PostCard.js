import React, { useState, useContext } from 'react';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import TextField from '@mui/material/TextField';
import Moment from 'react-moment';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';
import { Button } from '@mui/material';

const PostCard = ({ post, index }) => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { forumThread, updatePost, deletePost } = forumContext;
    const { user } = userContext;

    const canEdit = user.id === post.owner.id || user.userType === 'ADMIN';
    const [edit, setEdit] = useState(false);
    const [content, setContent] = useState(post.content);

    const onClickEdit = () => {
        setEdit(!edit);
    };

    const onChange = (e) => {
        setContent(e.target.value);
    };

    const onClickUpdate = () => {
        setEdit(false);
        setContent('');
        updatePost(
            {
                ...post,
                content: content
            },
            user.id
        );
    };

    const onClickDelete = () => {
        setEdit(false);
        setContent('');
        deletePost(post.id, user.id);
    };

    return (
        <Grid item xs={12} md={6}>
            <Card sx={{ display: 'flex', alignItems: 'center' }}>
                <CardContent sx={{ flex: 1 }}>
                    <Typography variant='subtitle2' color='text.secondary'>
                        {`${post.owner.username} • Created on `}
                        <Moment format='DD/MM/YYYY h:mma'>
                            {post.dateCreated.substring(
                                0,
                                post.dateCreated.length - 5
                            )}
                        </Moment>
                    </Typography>
                    <Divider />
                    {edit ? (
                        <Grid container spacing={2} xs={12} columns={12}>
                            <Grid item xs={12}>
                                <TextField
                                    label='Edit Post'
                                    name='content'
                                    defaultValue={post.content}
                                    fullWidth
                                    multiline
                                    rows={4}
                                    sx={{ mt: 3 }}
                                    onChange={onChange}
                                />{' '}
                            </Grid>
                            <Grid item xs={2}>
                                <Button
                                    variant='contained'
                                    onClick={onClickUpdate}
                                >
                                    Update Post
                                </Button>
                            </Grid>
                            <Grid item xs={2}>
                                <Button
                                    variant='outlined'
                                    color='error'
                                    onClick={onClickDelete}
                                >
                                    Delete Post
                                </Button>
                            </Grid>
                        </Grid>
                    ) : (
                        <Typography
                            variant='subtitle1'
                            paragraph
                            sx={{ margin: 1 }}
                        >
                            {post.content}
                        </Typography>
                    )}
                    {canEdit && (
                        <Button
                            size='small'
                            sx={{ mt: 2 }}
                            onClick={onClickEdit}
                        >
                            Edit Post
                        </Button>
                    )}
                </CardContent>
            </Card>
        </Grid>
    );
};

export default PostCard;
