import React, { useContext, useEffect, useState } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import PostAddIcon from '@mui/icons-material/PostAdd';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import PostCard from '../forum/PostCard';
import Footer from '../forum/Footer';

import AlertContext from '../../context/alert/alertContext';
import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField
} from '@mui/material';
import AlertBar from './AlertBar';

const theme = createTheme();

const ForumThread = (props) => {
    const alertContext = useContext(AlertContext);
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { setAlert } = alertContext;
    const {
        forumThread,
        createPost,
        updateForumThread,
        closeForumThread,
        deleteForumThread,
        error
    } = forumContext;
    const { user } = userContext;

    const [isReplyOpen, setIsReplyOpen] = useState(false);
    const [edit, setEdit] = useState(false);
    const [content, setContent] = useState('');
    const [editThread, setEditThread] = useState({
        ...forumThread
    });
    const [openDialog, setOpenDialog] = React.useState(false);

    const onClickReply = () => {
        setIsReplyOpen(!isReplyOpen);
    };

    const onClickEdit = () => {
        setEdit(!edit);
    };

    const onClickSave = () => {
        setEdit(false);
        updateForumThread(editThread, user.id);
    };

    const onClickCloseThread = (e, closed) => {
        closeForumThread(forumThread, user, closed);
    };

    const onClickPost = () => {
        setIsReplyOpen(false);
        setContent('');
        createPost(
            {
                owner: user,
                content
            },
            forumThread.id
        );
    };

    const onChangePost = (e) => {
        setContent(e.target.value);
    };

    const onChangeTitle = (e) => {
        setEditThread({
            ...editThread,
            title: e.target.value
        });
    };

    const handleClickOpen = () => {
        setOpenDialog(true);
    };

    const handleClose = (e, handleDelete) => {
        if (handleDelete) {
            deleteForumThread(forumThread.id, user.id);
            if (error == null) {
                props.history.push('/');
            } else {
                setAlert(error, 'error');
                setOpenDialog(false);
            }
        } else {
            setOpenDialog(false);
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                {edit ? (
                    <TextField
                        name='title'
                        label='Title'
                        defaultValue={forumThread.title}
                        onChange={onChangeTitle}
                        sx={{ mb: 3 }}
                    />
                ) : (
                    <Typography variant='h5' gutterBottom sx={{ mb: 3 }}>
                        {forumThread.title}
                    </Typography>
                )}

                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <AlertBar />
                    </Grid>
                    {(user.id === forumThread.owner.id ||
                        user.userType === 'ADMIN') && (
                        <Grid item xs={12}>
                            {edit ? (
                                <Button
                                    variant='contained'
                                    onClick={onClickSave}
                                >
                                    Save Thread
                                </Button>
                            ) : (
                                <Button
                                    variant='contained'
                                    onClick={onClickEdit}
                                >
                                    Edit Thread
                                </Button>
                            )}
                        </Grid>
                    )}
                    {forumThread.isClosed && (
                        <Grid item xs={12}>
                            <Typography
                                variant='subtitle2'
                                color='text.secondary'
                            >
                                This thread has been closed by{' '}
                                {forumThread.closedBy.username}
                            </Typography>
                        </Grid>
                    )}
                    {user.userType === 'ADMIN' && (
                        <Grid item xs={12}>
                            {forumThread.isClosed ? (
                                <Button
                                    onClick={(e) =>
                                        onClickCloseThread(e, false)
                                    }
                                >
                                    Open Thread
                                </Button>
                            ) : (
                                <Button
                                    onClick={(e) => onClickCloseThread(e, true)}
                                >
                                    Close Thread
                                </Button>
                            )}
                        </Grid>
                    )}
                </Grid>
                <main>
                    <Typography variant='h6' gutterBottom>
                        Posts
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Grid container spacing={4} columns={1}>
                        {forumThread.posts != null &&
                        forumThread.posts.length > 0 ? (
                            forumThread.posts.map((post, i) => (
                                <PostCard post={post} index={i} />
                            ))
                        ) : (
                            <Grid item xs={12}>
                                <Typography
                                    variant='subtitle1'
                                    color='text.secondary'
                                    gutterBottom
                                    sx={{ margin: 1 }}
                                >
                                    No posts in this thread!
                                </Typography>
                            </Grid>
                        )}
                        {!forumThread.isClosed && (
                            <Grid item xs={4}>
                                <Button
                                    variant='outlined'
                                    startIcon={<PostAddIcon />}
                                    onClick={onClickReply}
                                >
                                    Reply to Thread
                                </Button>
                            </Grid>
                        )}
                        {isReplyOpen && (
                            <Grid item xs={12}>
                                <TextField
                                    label='Post Content'
                                    name='content'
                                    fullWidth
                                    multiline
                                    rows={4}
                                    onChange={onChangePost}
                                />
                                <Button
                                    variant='contained'
                                    sx={{ mt: 1 }}
                                    onClick={onClickPost}
                                >
                                    Post Reply
                                </Button>
                            </Grid>
                        )}
                        {user.userType === 'ADMIN' && (
                            <Grid item xs={12}>
                                <Button
                                    variant='outlined'
                                    color='error'
                                    sx={{ mb: 2 }}
                                    onClick={handleClickOpen}
                                >
                                    Delete Thread
                                </Button>
                                <Dialog
                                    open={openDialog}
                                    onClose={handleClose}
                                    aria-labelledby='alert-dialog-title'
                                    aria-describedby='alert-dialog-description'
                                >
                                    <DialogTitle id='alert-dialog-title'>
                                        Delete Forum Thread
                                    </DialogTitle>
                                    <DialogContent>
                                        <DialogContentText id='alert-dialog-description'>
                                            Are you sure you want to delete this
                                            forum thread?
                                        </DialogContentText>
                                    </DialogContent>
                                    <DialogActions>
                                        <Button
                                            onClick={(e) =>
                                                handleClose(e, true)
                                            }
                                        >
                                            Yes
                                        </Button>
                                        <Button
                                            onClick={(e) =>
                                                handleClose(e, false)
                                            }
                                            autoFocus
                                        >
                                            No
                                        </Button>
                                    </DialogActions>
                                </Dialog>
                            </Grid>
                        )}
                    </Grid>
                </main>
            </Container>
            <Footer
                title='Footer'
                description='Something here to give the footer a purpose!'
            />
        </ThemeProvider>
    );
};
export default ForumThread;
